package com.learning.websocket.lifecycle.client.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.websocket-client")
public record WebSocketClientConfigurationProperties(String serverWsUrl, String sessionsApiUrl) {
}
