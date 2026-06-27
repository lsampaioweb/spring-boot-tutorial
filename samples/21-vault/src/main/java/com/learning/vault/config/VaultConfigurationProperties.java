package com.learning.vault.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.vault")
public record VaultConfigurationProperties(
    String uri,
    String token,
    String mountPath,
    String readPathTemplate,
    List<VaultSecretEntry> secrets
) {
}
