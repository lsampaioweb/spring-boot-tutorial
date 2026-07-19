package com.learning.vault.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import java.util.Collections;
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

  private static final String BASEURL = "http://localhost:8200";
  private static final String SECRETENDPOINT = BASEURL + "/v1/secret/data/spring-boot-tutorial";
  private static final String TOKEN = "tutorial-root-token";
  private static final String MOUNTPATH = "secret";
  private static final String READPATHTEMPLATE = "/v1/%s/data/%s";
  private static final String SECRETPATH = "spring-boot-tutorial";
  private static final String DBPASSWORDKEY = "db-password";
  private static final String APISECRETKEY = "api-secret";
  private static final String ERROR_REGISTRY_SECRETS_NOT_CONFIGURED = "error.vault.registry.secrets.not.configured";
  private static final String ERROR_REGISTRY_STARTUP_FAILED = "error.vault.registry.startup.failed";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private MockRestServiceServer server;
  private VaultSecretRegistry vaultSecretRegistry;
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
        List.of(
            new VaultSecretEntry(SECRETPATH, DBPASSWORDKEY),
            new VaultSecretEntry(SECRETPATH, APISECRETKEY)));

    RestClient restClient = builder.build();

    MessageSource messageSource = new ResourceBundleMessageSource();
    ((ResourceBundleMessageSource) messageSource).setBasename("i18n/messages");
    ((ResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
    logMessages = new LogMessages(messageSource);

    VaultSecretService vaultSecretService = new VaultSecretService(restClient, properties, logMessages);

    vaultSecretRegistry = new VaultSecretRegistry(vaultSecretService, properties, logMessages);
  }

  @Test
  void loadSecretsWhenSecretsConfigIsEmptyShouldFailFast() {
    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        BASEURL,
        TOKEN,
        MOUNTPATH,
        READPATHTEMPLATE,
        Collections.emptyList());

    RestClient restClient = RestClient.builder().baseUrl(BASEURL).build();
    VaultSecretService vaultSecretService = new VaultSecretService(restClient, properties, logMessages);
    VaultSecretRegistry registry = new VaultSecretRegistry(vaultSecretService, properties, logMessages);

    assertThatThrownBy(registry::loadSecrets)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
  }

  @Test
  void loadSecretsWhenSecretsConfigIsNullShouldFailFast() {
    VaultConfigurationProperties properties = new VaultConfigurationProperties(
        BASEURL,
        TOKEN,
        MOUNTPATH,
        READPATHTEMPLATE,
        null);

    RestClient restClient = RestClient.builder().baseUrl(BASEURL).build();
    VaultSecretService vaultSecretService = new VaultSecretService(restClient, properties, logMessages);
    VaultSecretRegistry registry = new VaultSecretRegistry(vaultSecretService, properties, logMessages);

    assertThatThrownBy(registry::loadSecrets)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_REGISTRY_SECRETS_NOT_CONFIGURED));
  }

  @Test
  void loadSecretsWhenAllSecretsExistShouldPopulateCache() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "my-db-password",
                  "api-secret": "my-api-secret"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    vaultSecretRegistry.loadSecrets();

    assertThat(vaultSecretRegistry.get(DBPASSWORDKEY)).isEqualTo("my-db-password");
    assertThat(vaultSecretRegistry.get(APISECRETKEY)).isEqualTo("my-api-secret");
    server.verify();
  }

  @Test
  void getWhenKeyNotLoadedShouldThrowException() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withSuccess("""
            {
              "data": {
                "data": {
                  "db-password": "my-db-password",
                  "api-secret": "my-api-secret"
                }
              }
            }
            """, MediaType.APPLICATION_JSON));

    vaultSecretRegistry.loadSecrets();

    assertThatThrownBy(() -> vaultSecretRegistry.get("unknown-key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_VAULT_SECRET_KEY_NOT_FOUND, "unknown-key"));
  }

  @Test
  void getWhenLocaleIsPortugueseShouldResolveLocalizedMessage() {
    java.util.Locale previousLocale = org.springframework.context.i18n.LocaleContextHolder.getLocale();
    org.springframework.context.i18n.LocaleContextHolder.setLocale(java.util.Locale.forLanguageTag("pt-BR"));

    try {
      assertThatThrownBy(() -> vaultSecretRegistry.get("unknown-key"))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining(expectedMessage(ERROR_VAULT_SECRET_KEY_NOT_FOUND, "unknown-key"));
    } finally {
      org.springframework.context.i18n.LocaleContextHolder.setLocale(previousLocale);
    }
  }

  @Test
  void loadSecretsWhenVaultReturnsErrorShouldThrowAndFailStartup() {
    server.expect(requestTo(SECRETENDPOINT))
        .andExpect(method(GET))
        .andRespond(withServerError());

    assertThatThrownBy(() -> vaultSecretRegistry.loadSecrets())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(expectedMessage(ERROR_REGISTRY_STARTUP_FAILED, SECRETPATH, "*"));
  }

  private String expectedMessage(String key, Object... args) {
    return logMessages.get(key, args);
  }
}
