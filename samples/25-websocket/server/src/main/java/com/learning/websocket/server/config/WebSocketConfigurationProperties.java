package com.learning.websocket.server.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Holds the explicit origins allowed to connect to the WebSocket endpoint. */
@ConfigurationProperties(prefix = "app.websocket")
public record WebSocketConfigurationProperties(List<String> allowedOrigins) {
}
