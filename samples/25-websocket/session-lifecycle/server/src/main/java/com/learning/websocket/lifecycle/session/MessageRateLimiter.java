package com.learning.websocket.lifecycle.session;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.learning.websocket.lifecycle.config.WebSocketConfigurationProperties;

/**
 * Sliding-window rate limiter keyed by STOMP session id.
 * Returns true when the caller should be force-disconnected.
 */
@Component
class MessageRateLimiter {

  private final ConcurrentMap<String, Deque<Instant>> timestampsBySession = new ConcurrentHashMap<>();
  private final int maxMessages;
  private final long windowMs;

  MessageRateLimiter(WebSocketConfigurationProperties properties) {
    this.maxMessages = properties.abuse().maxMessages();
    this.windowMs = properties.abuse().windowMs();
  }

  boolean exceedsLimit(String sessionId) {
    Instant now = Instant.now();
    Deque<Instant> timestamps = timestampsBySession.computeIfAbsent(sessionId, ignored -> new ArrayDeque<>());

    synchronized (timestamps) {
      while (!timestamps.isEmpty() && now.toEpochMilli() - timestamps.peekFirst().toEpochMilli() > windowMs) {
        timestamps.removeFirst();
      }

      timestamps.addLast(now);
      return timestamps.size() > maxMessages;
    }
  }

  void clear(String sessionId) {
    timestampsBySession.remove(sessionId);
  }
}
