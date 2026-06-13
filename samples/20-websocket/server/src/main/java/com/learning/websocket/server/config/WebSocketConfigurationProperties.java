package com.learning.websocket.server.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.websocket")
public record WebSocketConfigurationProperties(List<String> allowedOrigins) {
}
