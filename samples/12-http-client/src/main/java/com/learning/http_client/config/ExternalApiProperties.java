package com.learning.http_client.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.api")
public record ExternalApiProperties(
    String users,
    Duration connectTimeout,
    Duration readTimeout) {
}
