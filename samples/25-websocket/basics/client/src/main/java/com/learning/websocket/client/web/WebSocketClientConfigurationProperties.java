package com.learning.websocket.client.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Holds the configured WebSocket server URL used by the browser client. */
@ConfigurationProperties(prefix = "app.websocket-client")
public record WebSocketClientConfigurationProperties(String serverWsUrl) {
}
