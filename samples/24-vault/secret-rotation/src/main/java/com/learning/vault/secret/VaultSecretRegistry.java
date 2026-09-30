package com.learning.vault.secret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultVersionedKeyValueOperations;
import org.springframework.vault.support.Versioned;
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

  private final VaultOperations vaultOperations;
  private final VaultConfigurationProperties properties;
  private final LogMessages logMessages;
  private final AtomicReference<Map<String, String>> cacheRef = new AtomicReference<>(Map.of());

  VaultSecretRegistry(VaultOperations vaultOperations, VaultConfigurationProperties properties,
      LogMessages logMessages) {
    this.vaultOperations = vaultOperations;
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
    List<String> requiredSecrets = properties.requiredSecrets();
    Map<String, String> secrets = properties.secrets();
    if (requiredSecrets.isEmpty()) {
      throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
    }
    Map<String, String> nextCache = new HashMap<>();

    log.info(logMessages.get(LOG_REGISTRY_LOADING, requiredSecrets.size()));

    for (String key : requiredSecrets) {
      String value = secrets.get(key);
      if (value == null || value.isBlank()) {
        throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, key));
      }

      nextCache.put(key, value);
      log.debug(logMessages.get(LOG_REGISTRY_SECRET_LOADED, key));
    }

    cacheRef.set(Map.copyOf(nextCache));
    log.info(logMessages.get(LOG_REGISTRY_LOADED, nextCache.size()));
  }

  void refreshSecretsSafe() {
    List<String> requiredSecrets = properties.requiredSecrets();
    if (requiredSecrets.isEmpty()) {
      throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
    }
    Map<String, String> currentCache = cacheRef.get();
    Map<String, String> nextCache = new HashMap<>(currentCache);

    log.info(logMessages.get(LOG_REGISTRY_REFRESHING, requiredSecrets.size()));

    VaultVersionedKeyValueOperations keyValueOperations = vaultOperations
        .opsForVersionedKeyValue(properties.mountPath());
    Versioned<Map<String, Object>> versionedSecrets;
    try {
      versionedSecrets = keyValueOperations.get(properties.secretPath());
    } catch (RuntimeException ex) {
      log.warn(logMessages.get(ERROR_REGISTRY_REFRESH_FAILED, properties.secretPath(), "*"), ex);
      return;
    }

    Map<String, Object> refreshedSecrets = versionedSecrets == null ? null : versionedSecrets.getData();
    if (refreshedSecrets == null) {
      log.warn(logMessages.get(ERROR_REGISTRY_REFRESH_FAILED, properties.secretPath(), "*"));
      return;
    }

    boolean hasFailures = false;
    for (String key : requiredSecrets) {
      Object value = refreshedSecrets.get(key);
      if (value == null || String.valueOf(value).isBlank()) {
        log.warn(logMessages.get(ERROR_REGISTRY_REFRESH_FAILED, properties.secretPath(), key));
        hasFailures = true;
      } else {
        nextCache.put(key, String.valueOf(value));
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

}
