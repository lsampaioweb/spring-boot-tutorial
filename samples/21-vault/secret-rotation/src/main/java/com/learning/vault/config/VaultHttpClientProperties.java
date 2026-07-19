package com.learning.vault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.vault.http")
public record VaultHttpClientProperties(
    long connectTimeoutMs,
    long readTimeoutMs) {
}