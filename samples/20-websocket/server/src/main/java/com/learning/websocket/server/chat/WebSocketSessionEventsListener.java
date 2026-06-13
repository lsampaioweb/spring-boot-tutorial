package com.learning.websocket.server.chat;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketSessionEventsListener {

  private static final String LOG_WEBSOCKET_SESSION_CONNECTED = "log.websocket.session.connected";
  private static final String LOG_WEBSOCKET_SESSION_DISCONNECTED = "log.websocket.session.disconnected";

  private final ConnectionTracker connectionTracker;
  private final LogMessages logMessages;

  public WebSocketSessionEventsListener(ConnectionTracker connectionTracker, LogMessages logMessages) {
    this.connectionTracker = connectionTracker;
    this.logMessages = logMessages;
  }

  @EventListener
  public void onSessionConnected(SessionConnectedEvent event) {
    String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();

    connectionTracker.onConnect(sessionId);

    log.info(logMessages.get(LOG_WEBSOCKET_SESSION_CONNECTED, sessionId, connectionTracker.getOpenConnections()));
  }

  @EventListener
  public void onSessionDisconnected(SessionDisconnectEvent event) {
    connectionTracker.onDisconnect(event.getSessionId());

    log.info(logMessages.get(LOG_WEBSOCKET_SESSION_DISCONNECTED, event.getSessionId(),
        connectionTracker.getOpenConnections()));
  }

}
