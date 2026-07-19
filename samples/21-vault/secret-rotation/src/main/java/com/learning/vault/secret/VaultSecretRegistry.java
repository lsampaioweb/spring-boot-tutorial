package com.learning.vault.secret;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.config.VaultSecretEntry;
import com.learning.vault.i18n.LogMessages;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class VaultSecretRegistry implements SecretRegistry {

  private static final String LOG_REGISTRY_LOADING = "log.vault.registry.loading";
  private static final String LOG_REGISTRY_LOADED = "log.vault.registry.loaded";
  private static final String LOG_REGISTRY_SECRET_LOADED = "log.vault.registry.secret.loaded";
  private static final String LOG_REGISTRY_REFRESHING = "log.vault.registry.refreshing";
  private static final String LOG_REGISTRY_REFRESHED = "log.vault.registry.refreshed";
  private static final String LOG_REGISTRY_REFRESH_SKIPPED = "log.vault.registry.refresh.skipped";
  private static final String ERROR_REGISTRY_SECRETS_NOT_CONFIGURED = "error.vault.registry.secrets.not.configured";
  private static final String ERROR_REGISTRY_REFRESH_FAILED = "error.vault.registry.refresh.failed";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private final VaultSecretService vaultSecretService;
  private final VaultConfigurationProperties properties;
  private final LogMessages logMessages;
  private final AtomicReference<Map<String, String>> cacheRef = new AtomicReference<>(Map.of());

  VaultSecretRegistry(VaultSecretService vaultSecretService, VaultConfigurationProperties properties,
      LogMessages logMessages) {
    this.vaultSecretService = vaultSecretService;
    this.properties = properties;
    this.logMessages = logMessages;
  }

  @PostConstruct
  void loadSecrets() {
    loadSecretsStrict();
  }

  @Scheduled(fixedDelayString = "${app.vault.rotation.interval-ms:15000}", initialDelayString = "${app.vault.rotation.initial-delay-ms:15000}")
  void refreshSecrets() {
    VaultConfigurationProperties.Rotation rotation = properties.rotation();
    if (rotation != null && !rotation.enabled()) {
      log.debug(logMessages.get(LOG_REGISTRY_REFRESH_SKIPPED));
      return;
    }

    refreshSecretsSafe();
  }

  void loadSecretsStrict() {
    List<VaultSecretEntry> secrets = properties.secrets();
    if (secrets == null || secrets.isEmpty()) {
      throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
    }
    Map<String, String> nextCache = new HashMap<>();

    log.info(logMessages.get(LOG_REGISTRY_LOADING, secrets.size()));

    Map<String, List<VaultSecretEntry>> entriesByPath = groupByPath(secrets);

    for (Map.Entry<String, List<VaultSecretEntry>> pathGroup : entriesByPath.entrySet()) {
      String path = pathGroup.getKey();
      Map<String, String> secretValues;

      try {
        secretValues = vaultSecretService.readSecrets(path);
      } catch (RuntimeException ex) {
        throw new IllegalStateException(
            logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, path, "*"), ex);
      }

      for (VaultSecretEntry entry : pathGroup.getValue()) {
        Object value = secretValues.get(entry.key());
        if (value == null) {
          throw new IllegalStateException(
              logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, entry.path(), entry.key()));
        }

        nextCache.put(entry.key(), String.valueOf(value));
        log.debug(logMessages.get(LOG_REGISTRY_SECRET_LOADED, entry.path(), entry.key()));
      }
    }

    cacheRef.set(Map.copyOf(nextCache));
    log.info(logMessages.get(LOG_REGISTRY_LOADED, nextCache.size()));
  }

  void refreshSecretsSafe() {
    List<VaultSecretEntry> secrets = properties.secrets();
    if (secrets == null || secrets.isEmpty()) {
      throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
    }
    Map<String, String> currentCache = cacheRef.get();
    Map<String, String> nextCache = new HashMap<>(currentCache);
    boolean hasFailures = false;

    log.info(logMessages.get(LOG_REGISTRY_REFRESHING, secrets.size()));

    Map<String, List<VaultSecretEntry>> entriesByPath = groupByPath(secrets);

    for (Map.Entry<String, List<VaultSecretEntry>> pathGroup : entriesByPath.entrySet()) {
      String path = pathGroup.getKey();
      Map<String, String> secretValues;

      try {
        secretValues = vaultSecretService.readSecrets(path);
      } catch (RuntimeException ex) {
        log.warn(logMessages.get(ERROR_REGISTRY_REFRESH_FAILED, path, "*"), ex);
        hasFailures = true;
        continue;
      }

      for (VaultSecretEntry entry : pathGroup.getValue()) {
        Object value = secretValues.get(entry.key());
        if (value == null) {
          log.warn(logMessages.get(ERROR_REGISTRY_REFRESH_FAILED, entry.path(), entry.key()));
          hasFailures = true;
          continue;
        }
        nextCache.put(entry.key(), String.valueOf(value));
      }
    }

    if (hasFailures && nextCache.equals(currentCache)) {
      return;
    }

    cacheRef.set(Map.copyOf(nextCache));
    log.info(logMessages.get(LOG_REGISTRY_REFRESHED, nextCache.size()));
  }

  @Override
  public String get(String key) {
    String value = cacheRef.get().get(key);

    if (value == null) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, key));
    }

    return value;
  }

  private Map<String, List<VaultSecretEntry>> groupByPath(List<VaultSecretEntry> entries) {
    Map<String, List<VaultSecretEntry>> grouped = new LinkedHashMap<>();

    for (VaultSecretEntry entry : entries) {
      grouped.computeIfAbsent(entry.path(), ignored -> new java.util.ArrayList<>()).add(entry);
    }

    return grouped;
  }
}
