package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

class VaultSecretRegistryTest {

  private static final String ERROR_REGISTRY_SECRETS_NOT_CONFIGURED = "error.vault.registry.secrets.not.configured";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private LogMessages logMessages;

  @BeforeEach
  void setUp() {
    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    ((ResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
    logMessages = new LogMessages(messageSource);
  }

  @Test
  void validatesAllConfiguredSecretsAtStartup() {
    VaultSecretRegistry registry = registry(
        List.of("db-password", "api-secret"),
        Map.of("db-password", "test-db-password", "api-secret", "test-api-secret"));

    registry.validateRequiredSecrets();

    assertThat(registry.get("db-password")).isEqualTo("test-db-password");
    assertThat(registry.get("api-secret")).isEqualTo("test-api-secret");
  }

  @Test
  void startupFailsWhenRequiredSecretListIsEmpty() {
    VaultSecretRegistry registry = registry(List.of(), Map.of());

    assertThatThrownBy(registry::validateRequiredSecrets)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
  }

  @Test
  void startupFailsWhenRequiredSecretIsMissing() {
    VaultSecretRegistry registry = registry(List.of("db-password"), Map.of());

    assertThatThrownBy(registry::validateRequiredSecrets)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, "db-password"));
  }

  @Test
  void startupFailsWhenRequiredSecretIsBlank() {
    VaultSecretRegistry registry = registry(List.of("db-password"), Map.of("db-password", " "));

    assertThatThrownBy(registry::validateRequiredSecrets)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, "db-password"));
  }

  @Test
  void getWhenKeyIsNotRegisteredThrowsException() {
    VaultSecretRegistry registry = registry(List.of("db-password"), Map.of("db-password", "test-value"));

    assertThatThrownBy(() -> registry.get("unknown-key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, "unknown-key"));
  }

  private VaultSecretRegistry registry(List<String> requiredSecrets, Map<String, String> secrets) {
    VaultConfigurationProperties properties = new VaultConfigurationProperties(requiredSecrets, secrets);
    return new VaultSecretRegistry(properties, logMessages);
  }
}
