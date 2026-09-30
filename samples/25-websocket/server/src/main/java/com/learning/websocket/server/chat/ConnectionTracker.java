package com.learning.websocket.server.chat;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/** Tracks active WebSocket session IDs for connection counts. */
@Component
public class ConnectionTracker {

  // Thread-safe set that keeps only active WebSocket session IDs.
  private final Set<String> activeSessionIds = ConcurrentHashMap.newKeySet();

  /** Adds a nonblank session ID to the active connection set. */
  public void onConnect(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return;
    }

    activeSessionIds.add(sessionId);
  }

  /** Removes a nonblank session ID from the active connection set. */
  public void onDisconnect(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return;
    }

    activeSessionIds.remove(sessionId);
  }

  /** Returns the number of active WebSocket sessions. */
  public int getOpenConnections() {
    return activeSessionIds.size();
  }

}
