package com.learning.websocket.lifecycle.config;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import com.learning.websocket.lifecycle.session.WebSocketSessionStore;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketConfigurationProperties.class)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

  private static final String TOPIC_DESTINATION_PREFIX = "/topic";
  private static final String QUEUE_DESTINATION_PREFIX = "/queue";
  private static final String APP_DESTINATION_PREFIX = "/app";
  private static final String STOMP_ENDPOINT = "/ws";

  private final WebSocketConfigurationProperties webSocketConfigurationProperties;
  private final WebSocketSessionStore webSocketSessionStore;
  private final TaskScheduler webSocketHeartBeatScheduler;

  public WebSocketConfiguration(
      WebSocketConfigurationProperties webSocketConfigurationProperties,
      WebSocketSessionStore webSocketSessionStore,
      TaskScheduler webSocketHeartBeatScheduler) {
    this.webSocketConfigurationProperties = webSocketConfigurationProperties;
    this.webSocketSessionStore = webSocketSessionStore;
    this.webSocketHeartBeatScheduler = webSocketHeartBeatScheduler;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(TOPIC_DESTINATION_PREFIX, QUEUE_DESTINATION_PREFIX)
        .setHeartbeatValue(new long[] {
            webSocketConfigurationProperties.heartbeatSendIntervalMs(),
            webSocketConfigurationProperties.heartbeatReceiveIntervalMs()
        })
        .setTaskScheduler(webSocketHeartBeatScheduler);

    registry.setApplicationDestinationPrefixes(APP_DESTINATION_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_ENDPOINT)
        .setAllowedOriginPatterns(resolveAllowedOriginPatterns())
        .withSockJS();
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.addDecoratorFactory(this::decorateHandler);
  }

  private WebSocketHandler decorateHandler(WebSocketHandler handler) {
    return new WebSocketHandlerDecorator(handler) {
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessionStore.put(session.getId(), session);
        super.afterConnectionEstablished(session);
      }

      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        webSocketSessionStore.remove(session.getId());
        super.afterConnectionClosed(session, closeStatus);
      }
    };
  }

  private String[] resolveAllowedOriginPatterns() {
    List<String> allowedOrigins = webSocketConfigurationProperties.allowedOrigins();

    if (allowedOrigins == null || allowedOrigins.isEmpty()) {
      throw new IllegalStateException(
          "app.websocket.allowed-origins must be configured; refusing to register /ws without explicit origins");
    }

    for (String origin : allowedOrigins) {
      if ("*".equals(origin) || "/**".equals(origin)) {
        throw new IllegalStateException(
            "app.websocket.allowed-origins must not use a wildcard; configure explicit origins");
      }
    }

    return allowedOrigins.toArray(String[]::new);
  }
}
