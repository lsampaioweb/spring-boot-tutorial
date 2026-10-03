package com.learning.websocket.lifecycle.session;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
class WebSocketSessionEventsListener {

  private static final String DISPLAY_NAME_HEADER = "displayName";

  private final SessionService sessionService;

  WebSocketSessionEventsListener(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @EventListener
  void onSessionConnect(SessionConnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();
    String displayName = accessor.getFirstNativeHeader(DISPLAY_NAME_HEADER);
    sessionService.onConnect(sessionId, displayName);
  }

  @EventListener
  void onSessionDisconnect(SessionDisconnectEvent event) {
    sessionService.onDisconnect(event.getSessionId(), PresenceType.DISCONNECTED);
  }
}
