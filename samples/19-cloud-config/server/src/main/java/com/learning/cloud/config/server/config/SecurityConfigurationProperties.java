package com.learning.cloud.config.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.cloud-config-client")
public record SecurityConfigurationProperties(String username, String password) {
}
