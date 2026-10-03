package com.learning.websocket.lifecycle.session;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/** Holds raw WebSocket sessions so the server can force-close them by STOMP session id. */
@Component
public class WebSocketSessionStore {

  private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

  public void put(String sessionId, WebSocketSession session) {
    if (sessionId == null || sessionId.isBlank() || session == null) {
      return;
    }

    sessions.put(sessionId, session);
  }

  public void remove(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return;
    }

    sessions.remove(sessionId);
  }

  public Optional<WebSocketSession> find(String sessionId) {
    return Optional.ofNullable(sessions.get(sessionId));
  }

  public boolean close(String sessionId, CloseStatus closeStatus) throws IOException {
    WebSocketSession session = sessions.remove(sessionId);
    if (session == null || !session.isOpen()) {
      return false;
    }

    session.close(closeStatus);
    return true;
  }
}
