package com.learning.vault.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.vault")
public record VaultConfigurationProperties(
    String uri,
    String token,
    String mountPath,
    String readPathTemplate,
    Rotation rotation,
    List<VaultSecretEntry> secrets) {

  @Override
  public String toString() {
    return "VaultConfigurationProperties[uri=" + uri
        + ", token=[REDACTED]"
        + ", mountPath=" + mountPath
        + ", readPathTemplate=" + readPathTemplate
        + ", rotation=" + rotation
        + ", secrets=" + secrets + "]";
  }

  public record Rotation(
      boolean enabled,
      long intervalMs,
      long initialDelayMs) {
  }
}
