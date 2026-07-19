package com.learning.vault;

import com.learning.vault.secret.SecretRegistry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class VaultApplicationTests {

  @Autowired
  Environment environment;

  @MockitoBean
  SecretRegistry secretRegistry;

  @Test
  void applicationConfigurationShouldExposeExpectedDefaults() {
    Assertions.assertAll(
        () -> Assertions.assertEquals("vault-multiple-secrets", environment.getProperty("spring.application.name")),
        () -> Assertions.assertEquals("8092", environment.getProperty("server.port")),
        () -> Assertions.assertEquals("i18n/messages", environment.getProperty("spring.messages.basename")),
        () -> Assertions.assertEquals("en", environment.getProperty("spring.messages.default-locale")),
        () -> Assertions.assertEquals("false", environment.getProperty("spring.messages.fallback-to-system-locale")),
        () -> Assertions.assertEquals("2000", environment.getProperty("app.vault.http.connect-timeout-ms")),
        () -> Assertions.assertEquals("5000", environment.getProperty("app.vault.http.read-timeout-ms")));
  }
}
