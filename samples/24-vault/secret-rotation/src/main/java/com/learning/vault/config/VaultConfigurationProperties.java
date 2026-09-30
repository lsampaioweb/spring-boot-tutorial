package com.learning.vault.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds Vault KV settings, imported secrets, and refresh behavior for the
 * sample.
 */
@ConfigurationProperties(prefix = "app.vault")
public record VaultConfigurationProperties(
    String mountPath,
    String secretPath,
    List<String> requiredSecrets,
    Map<String, String> secrets,
    Rotation rotation) {

  public VaultConfigurationProperties {
    requiredSecrets = requiredSecrets == null ? List.of() : List.copyOf(requiredSecrets);
    secrets = secrets == null ? Map.of() : Map.copyOf(secrets);
  }

  /** Defines whether and how often the sample refreshes static KV secrets. */
  public record Rotation(
      boolean enabled,
      long intervalMs,
      long initialDelayMs) {
  }
}
