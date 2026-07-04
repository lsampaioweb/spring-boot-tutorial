package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class VaultSecretServiceTest {

  private MockRestServiceServer server;
  private VaultSecretService vaultSecretService;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8200");
    server = MockRestServiceServer.bindTo(builder).build();

    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        "http://localhost:8200",
        "tutorial-root-token",
        "secret",
        "/v1/%s/data/%s",
        java.util.List.of());

    RestClient restClient = builder.build();

    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    LogMessages logMessages = new LogMessages(messageSource);

    vaultSecretService = new VaultSecretService(restClient, properties, logMessages);
  }

  @Test
  void readSecret_whenSecretExists_shouldReturnSecretValue() {
    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "super-secret-password"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    String secret = vaultSecretService.readSecret("spring-boot-tutorial", "db-password");

    assertThat(secret).isEqualTo("super-secret-password");
    server.verify();
  }

  @Test
  void readSecret_whenSecretKeyIsMissing_shouldThrowException() {
    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "another-key": "value"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    assertThatThrownBy(() -> vaultSecretService.readSecret("spring-boot-tutorial", "db-password"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Secret key not found: db-password");

    server.verify();
  }
}
