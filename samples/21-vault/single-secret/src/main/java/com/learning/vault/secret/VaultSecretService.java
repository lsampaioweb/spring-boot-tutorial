package com.learning.vault.secret;

import java.util.Map;

import com.learning.vault.config.VaultConfigurationProperties;
import com.learning.vault.i18n.LogMessages;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
class VaultSecretService {

  private static final String RESPONSE_FIELD_DATA = "data";

  private static final String ERROR_VAULT_RESPONSE_EMPTY = "error.vault.response.empty";
  private static final String ERROR_VAULT_RESPONSE_ACCESS_DENIED = "error.vault.response.access.denied";
  private static final String ERROR_VAULT_RESPONSE_PATH_NOT_FOUND = "error.vault.response.path.not.found";
  private static final String ERROR_VAULT_RESPONSE_SERVER_ERROR = "error.vault.response.server.error";
  private static final String ERROR_VAULT_RESPONSE_MISSING_DATA_PAYLOAD = "error.vault.response.missing.data.payload";
  private static final String ERROR_VAULT_RESPONSE_MISSING_SECRET_VALUES = "error.vault.response.missing.secret.values";
  private static final String ERROR_VAULT_SECRET_KEY_NOT_FOUND = "error.vault.secret.key.not.found";

  private final RestClient vaultRestClient;
  private final VaultConfigurationProperties properties;
  private final LogMessages logMessages;

  public VaultSecretService(RestClient vaultRestClient, VaultConfigurationProperties properties,
      LogMessages logMessages) {
    this.vaultRestClient = vaultRestClient;
    this.properties = properties;
    this.logMessages = logMessages;
  }

  String readSecret(String secretPath, String secretKey) {
    Map<String, String> secretValues = readSecrets(secretPath);

    Object value = secretValues.get(secretKey);
    if (value == null) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_SECRET_KEY_NOT_FOUND, secretKey));
    }

    return String.valueOf(value);
  }

  Map<String, String> readSecrets(String secretPath) {
    String endpoint = properties.readPathTemplate().formatted(properties.mountPath(), secretPath);

    Map<String, Object> response = vaultRestClient
        .get()
        .uri(endpoint)
        .retrieve()
        .onStatus(status -> status.value() == 403,
            (request, responseSpec) -> {
              throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_ACCESS_DENIED, secretPath));
            })
        .onStatus(status -> status.value() == 404,
            (request, responseSpec) -> {
              throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_PATH_NOT_FOUND, secretPath));
            })
        .onStatus(HttpStatusCode::is5xxServerError,
            (request, responseSpec) -> {
              throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_SERVER_ERROR, secretPath));
            })
        .body(new ParameterizedTypeReference<>() {
        });

    if (response == null) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_EMPTY));
    }

    Object dataContainer = response.get(RESPONSE_FIELD_DATA);
    if (!(dataContainer instanceof Map<?, ?> dataMap)) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_MISSING_DATA_PAYLOAD));
    }

    Object secrets = dataMap.get(RESPONSE_FIELD_DATA);
    if (!(secrets instanceof Map<?, ?> secretValues)) {
      throw new IllegalStateException(logMessages.get(ERROR_VAULT_RESPONSE_MISSING_SECRET_VALUES));
    }

    Map<String, String> mapped = new java.util.HashMap<>();
    for (Map.Entry<?, ?> entry : secretValues.entrySet()) {
      if (entry.getKey() != null && entry.getValue() != null) {
        mapped.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
      }
    }

    return mapped;
  }
}
