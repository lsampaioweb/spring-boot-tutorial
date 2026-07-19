package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
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

  private static final String BASEURL = "http://localhost:8200";
  private static final String SECRETENDPOINT = BASEURL + "/v1/secret/data/spring-boot-tutorial";
  private static final String TOKEN = "tutorial-root-token";
  private static final String MOUNTPATH = "secret";
  @SuppressWarnings("java:S1075")
  private static final String READPATHTEMPLATE = "/v1/%s/data/%s";
  private static final String SECRETPATH = "spring-boot-tutorial";
  private static final String DBPASSWORDKEY = "db-password";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";
  private static final String ERROR_VAULT_RESPONSE_EMPTY = "error.vault.response.empty";
  private static final String ERROR_VAULT_RESPONSE_MISSING_DATA_PAYLOAD = "error.vault.response.missing.data.payload";
  private static final String ERROR_VAULT_RESPONSE_MISSING_SECRET_VALUES = "error.vault.response.missing.secret.values";

  private MockRestServiceServer server;
  private VaultSecretService vaultSecretService;
  private LogMessages logMessages;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder().baseUrl(BASEURL);
    server = MockRestServiceServer.bindTo(builder).build();

    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        BASEURL,
        TOKEN,
        MOUNTPATH,
        READPATHTEMPLATE,
        new VaultConfigurationProperties.Rotation(true, 15000, 15000),
        java.util.List.of());

    RestClient restClient = builder.build();

    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    ((ResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
    logMessages = new LogMessages(messageSource);

    vaultSecretService = new VaultSecretService(restClient, properties, logMessages);
  }

  @Test
  void readSecretWhenSecretExistsShouldReturnSecretValue() {
    server.expect(requestTo(SECRETENDPOINT))
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

    String secret = vaultSecretService.readSecret(SECRETPATH, DBPASSWORDKEY);

    assertThat(secret).isEqualTo("super-secret-password");
    server.verify();
  }

  @Test
  void readSecretWhenSecretKeyIsMissingShouldThrowException() {
    server.expect(requestTo(SECRETENDPOINT))
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

    assertThatThrownBy(() -> vaultSecretService.readSecret(SECRETPATH, DBPASSWORDKEY))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_VAULT_SECRET_KEY_NOT_FOUND, DBPASSWORDKEY));

    server.verify();
  }

  @Test
  void readSecretWhenResponseIsEmptyShouldThrowException() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withNoContent());

    assertThatThrownBy(() -> vaultSecretService.readSecret(SECRETPATH, DBPASSWORDKEY))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_VAULT_RESPONSE_EMPTY));

    server.verify();
  }

  @Test
  void readSecretWhenDataPayloadIsMissingShouldThrowException() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

    assertThatThrownBy(() -> vaultSecretService.readSecret(SECRETPATH, DBPASSWORDKEY))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_VAULT_RESPONSE_MISSING_DATA_PAYLOAD));

    server.verify();
  }

  @Test
  void readSecretWhenSecretValuesAreMissingShouldThrowException() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
              }
            }
            """, MediaType.APPLICATION_JSON));

    assertThatThrownBy(() -> vaultSecretService.readSecret(SECRETPATH, DBPASSWORDKEY))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_VAULT_RESPONSE_MISSING_SECRET_VALUES));

    server.verify();
  }

  @Test
  void readSecretsWhenPathContainsMultipleKeysShouldReturnMap() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "super-secret-password",
                  "api-secret": "api-value"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    assertThat(vaultSecretService.readSecrets(SECRETPATH))
        .containsEntry(DBPASSWORDKEY, "super-secret-password")
        .containsEntry("api-secret", "api-value");

    server.verify();
  }

  private String expectedMessage(String key, Object... args) {
    return logMessages.get(key, args);
  }
}
