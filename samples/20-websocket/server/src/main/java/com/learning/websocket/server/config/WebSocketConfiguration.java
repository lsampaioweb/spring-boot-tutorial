package com.learning.websocket.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketConfigurationProperties.class)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

  private final WebSocketConfigurationProperties webSocketConfigurationProperties;

  public WebSocketConfiguration(WebSocketConfigurationProperties webSocketConfigurationProperties) {
    this.webSocketConfigurationProperties = webSocketConfigurationProperties;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic", "/queue");
    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    List<String> allowedOrigins = webSocketConfigurationProperties.allowedOrigins();
    String[] allowedOriginPatterns = (allowedOrigins == null || allowedOrigins.isEmpty())
        ? new String[] { "*" }
        : allowedOrigins.toArray(String[]::new);

    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns(allowedOriginPatterns)
        .withSockJS();
  }

}
