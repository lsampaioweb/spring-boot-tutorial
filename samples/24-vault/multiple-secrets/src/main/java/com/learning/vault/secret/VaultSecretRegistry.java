package com.learning.vault.secret;

import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.springframework.stereotype.Component;

@Slf4j
@Component
class VaultSecretRegistry implements SecretRegistry {

  private static final String LOG_REGISTRY_LOADING = "log.vault.registry.loading";
  private static final String LOG_REGISTRY_LOADED = "log.vault.registry.loaded";
  private static final String LOG_REGISTRY_SECRET_LOADED = "log.vault.registry.secret.loaded";
  private static final String ERROR_REGISTRY_SECRETS_NOT_CONFIGURED = "error.vault.registry.secrets.not.configured";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private final VaultConfigurationProperties properties;
  private final LogMessages logMessages;

  VaultSecretRegistry(VaultConfigurationProperties properties, LogMessages logMessages) {
    this.properties = properties;
    this.logMessages = logMessages;
  }

  @PostConstruct
  void validateRequiredSecrets() {
    List<String> requiredSecrets = properties.requiredSecrets();
    Map<String, String> secrets = properties.secrets();

    if (requiredSecrets.isEmpty()) {
      throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
    }

    log.info(logMessages.get(LOG_REGISTRY_LOADING, requiredSecrets.size()));

    for (String key : requiredSecrets) {
      String value = secrets.get(key);
      if (value == null || value.isBlank()) {
        throw new IllegalStateException(logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, key));
      }

      log.debug(logMessages.get(LOG_REGISTRY_SECRET_LOADED, key));
    }

    log.info(logMessages.get(LOG_REGISTRY_LOADED, requiredSecrets.size()));
  }

  @Override
  public String get(String key) {
    String value = properties.secrets().get(key);

    if (value == null) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, key));
    }

    return value;
  }

}
