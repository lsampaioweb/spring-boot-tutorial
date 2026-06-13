package com.learning.websocket.server.chat;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ConnectionTracker {

  // Thread-safe set that keeps only active WebSocket session IDs.
  private final Set<String> activeSessionIds = ConcurrentHashMap.newKeySet();

  public void onConnect(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return;
    }

    activeSessionIds.add(sessionId);
  }

  public void onDisconnect(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return;
    }

    activeSessionIds.remove(sessionId);
  }

  public int getOpenConnections() {
    return activeSessionIds.size();
  }

}
