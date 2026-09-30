package com.learning.vault;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.vault.core.VaultOperations;

@SpringBootTest(properties = {
    "spring.cloud.vault.enabled=false",
    "spring.cloud.vault.token=test-vault-token",
    "app.vault.required-secrets[0]=api-secret",
    "app.vault.required-secrets[1]=db-password",
    "app.vault.secrets.api-secret=test-api-secret",
    "app.vault.secrets.db-password=test-db-password"
})
@ActiveProfiles("test")
class VaultApplicationTests {

  @Autowired
  Environment environment;

  @MockitoBean
  VaultOperations vaultOperations;

  @Test
  void applicationConfigurationShouldExposeExpectedDefaults() {
    Assertions.assertAll(
        () -> Assertions.assertEquals("vault-secret-rotation", environment.getProperty("spring.application.name")),
        () -> Assertions.assertEquals("8093", environment.getProperty("server.port")),
        () -> Assertions.assertEquals("i18n/messages", environment.getProperty("spring.messages.basename")),
        () -> Assertions.assertEquals("en", environment.getProperty("spring.messages.default-locale")),
        () -> Assertions.assertEquals("false", environment.getProperty("spring.messages.fallback-to-system-locale")),
        () -> Assertions.assertEquals("2", environment.getProperty("spring.cloud.vault.kv.backend-version")),
        () -> Assertions.assertEquals("secret", environment.getProperty("spring.cloud.vault.kv.backend")),
        () -> Assertions.assertEquals("15000", environment.getProperty("app.vault.rotation.interval-ms")));
  }
}
