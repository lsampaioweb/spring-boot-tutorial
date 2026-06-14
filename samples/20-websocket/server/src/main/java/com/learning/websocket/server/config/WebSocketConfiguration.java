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

  private static final String TOPIC_DESTINATION_PREFIX = "/topic";
  private static final String QUEUE_DESTINATION_PREFIX = "/queue";
  private static final String APP_DESTINATION_PREFIX = "/app";
  private static final String STOMP_ENDPOINT = "/ws";

  private final WebSocketConfigurationProperties webSocketConfigurationProperties;

  public WebSocketConfiguration(WebSocketConfigurationProperties webSocketConfigurationProperties) {
    this.webSocketConfigurationProperties = webSocketConfigurationProperties;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // Broker destinations used by clients to receive pushed messages.
    registry.enableSimpleBroker(TOPIC_DESTINATION_PREFIX, QUEUE_DESTINATION_PREFIX);

    // Prefix used by clients when sending messages to server-side @MessageMapping handlers.
    registry.setApplicationDestinationPrefixes(APP_DESTINATION_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_ENDPOINT)
        .setAllowedOriginPatterns(resolveAllowedOriginPatterns())
        .withSockJS();
  }

  private String[] resolveAllowedOriginPatterns() {
    List<String> allowedOrigins = webSocketConfigurationProperties.allowedOrigins();

    if (hasNoConfiguredOrigins(allowedOrigins)) {
      return new String[] { "*" };
    }

    return allowedOrigins.toArray(String[]::new);
  }

  private boolean hasNoConfiguredOrigins(List<String> allowedOrigins) {
    return allowedOrigins == null || allowedOrigins.isEmpty();
  }

}
