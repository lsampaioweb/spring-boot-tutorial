package com.learning.websocket.lifecycle.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
class SessionRegistry {

  private final ConcurrentMap<String, SessionRecord> sessions = new ConcurrentHashMap<>();

  SessionRecord register(String sessionId, String displayName) {
    SessionRecord record = new SessionRecord(
        sessionId,
        displayName == null || displayName.isBlank() ? "anonymous" : displayName.trim(),
        Instant.now());
    sessions.put(sessionId, record);
    return record;
  }

  Optional<SessionRecord> remove(String sessionId) {
    return Optional.ofNullable(sessions.remove(sessionId));
  }

  Optional<SessionRecord> find(String sessionId) {
    return Optional.ofNullable(sessions.get(sessionId));
  }

  List<SessionRecord> findAll() {
    return new ArrayList<>(sessions.values());
  }

  int count() {
    return sessions.size();
  }
}
