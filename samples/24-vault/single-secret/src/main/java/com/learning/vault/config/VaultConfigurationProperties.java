package com.learning.vault.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Holds required secret keys and values imported from Vault into the Spring Environment. */
@ConfigurationProperties(prefix = "app.vault")
public record VaultConfigurationProperties(
    List<String> requiredSecrets,
    Map<String, String> secrets) {

  public VaultConfigurationProperties {
    requiredSecrets = requiredSecrets == null ? List.of() : List.copyOf(requiredSecrets);
    secrets = secrets == null ? Map.of() : Map.copyOf(secrets);
  }
}
