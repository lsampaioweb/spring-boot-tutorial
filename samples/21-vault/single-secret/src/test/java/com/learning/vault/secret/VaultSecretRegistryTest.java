package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import java.util.List;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.config.VaultSecretEntry;
import com.learning.vault.i18n.LogMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class VaultSecretRegistryTest {

  private MockRestServiceServer server;
  private VaultSecretRegistry vaultSecretRegistry;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8200");
    server = MockRestServiceServer.bindTo(builder).build();

    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        "http://localhost:8200",
        "tutorial-root-token",
        "secret",
        "/v1/%s/data/%s",
        List.of(
            new VaultSecretEntry("spring-boot-tutorial", "db-password"),
            new VaultSecretEntry("spring-boot-tutorial", "api-secret")));

    RestClient restClient = builder.build();

    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    LogMessages logMessages = new LogMessages(messageSource);

    VaultSecretService vaultSecretService = new VaultSecretService(restClient, properties, logMessages);

    vaultSecretRegistry = new VaultSecretRegistry(vaultSecretService, properties, logMessages);
  }

  @Test
  void loadSecrets_whenAllSecretsExist_shouldPopulateCache() {
    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "my-db-password"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "api-secret": "my-api-secret"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    vaultSecretRegistry.loadSecrets();

    assertThat(vaultSecretRegistry.get("db-password")).isEqualTo("my-db-password");
    assertThat(vaultSecretRegistry.get("api-secret")).isEqualTo("my-api-secret");
    server.verify();
  }

  @Test
  void get_whenKeyNotLoaded_shouldThrowException() {
    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "my-db-password"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "api-secret": "my-api-secret"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    vaultSecretRegistry.loadSecrets();

    assertThatThrownBy(() -> vaultSecretRegistry.get("unknown-key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Secret key not found: unknown-key");
  }

  @Test
  void loadSecrets_whenVaultReturnsError_shouldThrowAndFailStartup() {
    server.expect(requestTo("http://localhost:8200/v1/secret/data/spring-boot-tutorial"))
        .andExpect(method(GET))
        .andRespond(withServerError());

    assertThatThrownBy(() -> vaultSecretRegistry.loadSecrets())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to load Vault secret at startup");
  }
}
