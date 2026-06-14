package com.learning.websocket.client.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.websocket-client")
public record WebSocketClientConfigurationProperties(String serverWsUrl) {
}
