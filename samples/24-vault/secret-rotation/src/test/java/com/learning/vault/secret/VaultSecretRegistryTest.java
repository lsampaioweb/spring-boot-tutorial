package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultVersionedKeyValueOperations;
import org.springframework.vault.support.Versioned;

class VaultSecretRegistryTest {

  private static final String MOUNT_PATH = "secret";
  private static final String SECRET_PATH = "spring-boot-tutorial";
  private static final String DB_PASSWORD_KEY = "db-password";
  private static final String API_SECRET_KEY = "api-secret";
  private static final String ERROR_REGISTRY_SECRETS_NOT_CONFIGURED = "error.vault.registry.secrets.not.configured";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private VaultOperations vaultOperations;
  private VaultVersionedKeyValueOperations keyValueOperations;
  private LogMessages logMessages;

  @BeforeEach
  void setUp() {
    vaultOperations = mock(VaultOperations.class);
    keyValueOperations = mock(VaultVersionedKeyValueOperations.class);
    when(vaultOperations.opsForVersionedKeyValue(MOUNT_PATH)).thenReturn(keyValueOperations);

    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    ((ResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
    logMessages = new LogMessages(messageSource);
  }

  @Test
  void startupLoadsRequiredSecretsFromSpringEnvironment() {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY, API_SECRET_KEY), initialSecrets());

    registry.loadSecretsStrict();

    assertThat(registry.get(DB_PASSWORD_KEY)).isEqualTo("initial-db-password");
    assertThat(registry.get(API_SECRET_KEY)).isEqualTo("initial-api-secret");
    verifyNoInteractions(vaultOperations);
  }

  @Test
  void startupFailsWhenRequiredSecretsAreNotConfigured() {
    VaultSecretRegistry registry = registry(true, List.of(), Map.of());

    assertThatThrownBy(registry::loadSecretsStrict)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
  }

  @Test
  void startupFailsWhenRequiredSecretIsMissing() {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY), Map.of());

    assertThatThrownBy(registry::loadSecretsStrict)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_REGISTRY_STARTUP_FAILED, DB_PASSWORD_KEY));
  }

  @Test
  void getWhenKeyIsUnknownThrowsException() {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY), initialSecrets());
    registry.loadSecretsStrict();

    assertThatThrownBy(() -> registry.get("unknown-key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, "unknown-key"));
  }

  @Test
  void refreshFailureRetainsLastKnownGoodValues() {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY, API_SECRET_KEY), initialSecrets());
    registry.loadSecretsStrict();
    when(keyValueOperations.get(SECRET_PATH)).thenThrow(new IllegalStateException("Vault unavailable"));

    registry.refreshSecretsSafe();

    assertThat(registry.get(DB_PASSWORD_KEY)).isEqualTo("initial-db-password");
    assertThat(registry.get(API_SECRET_KEY)).isEqualTo("initial-api-secret");
  }

  @Test
  void partialRefreshUpdatesAvailableValuesAndRetainsMissingValues() {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY, API_SECRET_KEY), initialSecrets());
    registry.loadSecretsStrict();
    when(keyValueOperations.get(SECRET_PATH))
        .thenReturn(Versioned.create(Map.of(DB_PASSWORD_KEY, "rotated-db-password")));

    registry.refreshSecretsSafe();

    assertThat(registry.get(DB_PASSWORD_KEY)).isEqualTo("rotated-db-password");
    assertThat(registry.get(API_SECRET_KEY)).isEqualTo("initial-api-secret");
  }

  @Test
  void concurrentReadersNeverObserveMissingKeysDuringRefresh() throws InterruptedException {
    VaultSecretRegistry registry = registry(true, List.of(DB_PASSWORD_KEY, API_SECRET_KEY), initialSecrets());
    registry.loadSecretsStrict();
    when(keyValueOperations.get(SECRET_PATH)).thenReturn(Versioned.create(Map.of(
        DB_PASSWORD_KEY, "rotated-db-password",
        API_SECRET_KEY, "rotated-api-secret")));

    int iterations = 200;
    CountDownLatch start = new CountDownLatch(1);
    AtomicReference<Exception> failure = new AtomicReference<>();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    executor.submit(() -> {
      try {
        start.await();
        for (int index = 0; index < iterations; index++) {
          registry.get(DB_PASSWORD_KEY);
          registry.get(API_SECRET_KEY);
        }
      } catch (Exception ex) {
        failure.compareAndSet(null, ex);
      }
    });
    executor.submit(() -> {
      try {
        start.await();
        registry.refreshSecretsSafe();
      } catch (Exception ex) {
        failure.compareAndSet(null, ex);
      }
    });

    start.countDown();
    executor.shutdown();

    assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
    assertThat(failure.get()).isNull();
    assertThat(registry.get(DB_PASSWORD_KEY)).isEqualTo("rotated-db-password");
    assertThat(registry.get(API_SECRET_KEY)).isEqualTo("rotated-api-secret");
  }

  @Test
  void scheduledRefreshDoesNothingWhenRotationIsDisabled() {
    VaultSecretRegistry registry = registry(false, List.of(DB_PASSWORD_KEY), initialSecrets());

    registry.refreshSecrets();

    verify(vaultOperations, never()).opsForVersionedKeyValue(MOUNT_PATH);
  }

  private VaultSecretRegistry registry(boolean rotationEnabled, List<String> requiredSecrets,
      Map<String, String> secrets) {
    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        MOUNT_PATH,
        SECRET_PATH,
        requiredSecrets,
        secrets,
        new VaultConfigurationProperties.Rotation(rotationEnabled, 15000, 15000));
    return new VaultSecretRegistry(vaultOperations, properties, logMessages);
  }

  private Map<String, String> initialSecrets() {
    return Map.of(DB_PASSWORD_KEY, "initial-db-password", API_SECRET_KEY, "initial-api-secret");
  }
}
