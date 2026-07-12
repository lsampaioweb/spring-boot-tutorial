package com.learning.vault.secret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.config.VaultSecretEntry;
import com.learning.vault.i18n.LogMessages;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VaultSecretRegistry {

  private static final String LOG_REGISTRY_LOADING = "log.vault.registry.loading";
  private static final String LOG_REGISTRY_LOADED = "log.vault.registry.loaded";
  private static final String LOG_REGISTRY_SECRET_LOADED = "log.vault.registry.secret.loaded";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private final VaultSecretService vaultSecretService;
  private final VaultConfigurationProperties properties;
  private final LogMessages logMessages;
  private final Map<String, String> cache = new HashMap<>();

  VaultSecretRegistry(VaultSecretService vaultSecretService, VaultConfigurationProperties properties,
      LogMessages logMessages) {
    this.vaultSecretService = vaultSecretService;
    this.properties = properties;
    this.logMessages = logMessages;
  }

  @PostConstruct
  void loadSecrets() {
    List<VaultSecretEntry> secrets = properties.secrets();

    log.info(logMessages.get(LOG_REGISTRY_LOADING, secrets.size()));

    for (VaultSecretEntry entry : secrets) {
      try {
        String value = vaultSecretService.readSecret(entry.path(), entry.key());

        cache.put(entry.key(), value);
        log.debug(logMessages.get(LOG_REGISTRY_SECRET_LOADED, entry.path(), entry.key()));
      } catch (RuntimeException ex) {
        throw new IllegalStateException(
            logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, entry.path(), entry.key(), ex.getMessage()), ex);
      }
    }

    log.info(logMessages.get(LOG_REGISTRY_LOADED, cache.size()));
  }

  public String get(String key) {
    String value = cache.get(key);

    if (value == null) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, key));
    }

    return value;
  }
}
