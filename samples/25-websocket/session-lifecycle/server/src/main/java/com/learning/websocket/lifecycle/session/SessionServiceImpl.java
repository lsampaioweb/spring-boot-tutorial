package com.learning.websocket.lifecycle.session;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import com.learning.websocket.lifecycle.i18n.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class SessionServiceImpl implements SessionService {

  private static final String PRESENCE_TOPIC = "/topic/presence";
  private static final String LOG_SESSION_CONNECTED = "log.session.connected";
  private static final String LOG_SESSION_DISCONNECTED = "log.session.disconnected";
  private static final String LOG_SESSION_FORCE_DISCONNECT = "log.session.forceDisconnect";
  private static final String LOG_SESSION_RATE_LIMITED = "log.session.rateLimited";

  private final SessionRegistry sessionRegistry;
  private final SessionMapper sessionMapper;
  private final WebSocketSessionStore webSocketSessionStore;
  private final MessageRateLimiter messageRateLimiter;
  private final SimpMessagingTemplate messagingTemplate;
  private final LogMessages logMessages;
  private final ConcurrentMap<String, PresenceType> pendingDisconnectReasons = new ConcurrentHashMap<>();

  @Override
  public void onConnect(String sessionId, String displayName) {
    SessionRecord record = sessionRegistry.register(sessionId, displayName);
    log.info(logMessages.get(LOG_SESSION_CONNECTED, record.sessionId(), record.displayName(), sessionRegistry.count()));
    publish(PresenceType.CONNECTED, record);
  }

  @Override
  public void onDisconnect(String sessionId, PresenceType fallbackType) {
    messageRateLimiter.clear(sessionId);
    PresenceType type = pendingDisconnectReasons.getOrDefault(sessionId, fallbackType);
    pendingDisconnectReasons.remove(sessionId);

    sessionRegistry.remove(sessionId).ifPresent(record -> {
      log.info(logMessages.get(LOG_SESSION_DISCONNECTED, record.sessionId(), record.displayName(),
          sessionRegistry.count(), type.name()));
      publish(type, record);
    });
  }

  @Override
  public List<SessionResponse> listSessions() {
    return sessionRegistry.findAll().stream().map(sessionMapper::toResponse).toList();
  }

  @Override
  public void forceDisconnect(String sessionId) {
    SessionRecord record = sessionRegistry.find(sessionId)
        .orElseThrow(() -> new SessionNotFoundException(sessionId));

    log.info(logMessages.get(LOG_SESSION_FORCE_DISCONNECT, record.sessionId(), record.displayName()));
    closeSession(sessionId, CloseStatus.NORMAL.withReason("kicked by admin"), PresenceType.KICKED);
  }

  @Override
  public void handleClientMessage(String sessionId) {
    if (!messageRateLimiter.exceedsLimit(sessionId)) {
      return;
    }

    SessionRecord record = sessionRegistry.find(sessionId).orElse(null);
    String displayName = record == null ? "unknown" : record.displayName();
    log.warn(logMessages.get(LOG_SESSION_RATE_LIMITED, sessionId, displayName));
    closeSession(sessionId, CloseStatus.POLICY_VIOLATION.withReason("rate limit exceeded"), PresenceType.RATE_LIMITED);
  }

  private void closeSession(String sessionId, CloseStatus closeStatus, PresenceType presenceType) {
    pendingDisconnectReasons.put(sessionId, presenceType);

    try {
      boolean closed = webSocketSessionStore.close(sessionId, closeStatus);
      if (!closed) {
        onDisconnect(sessionId, presenceType);
      }
    } catch (IOException exception) {
      onDisconnect(sessionId, presenceType);
    }
  }

  private void publish(PresenceType type, SessionRecord record) {
    messagingTemplate.convertAndSend(
        PRESENCE_TOPIC,
        new PresenceEvent(type, record.sessionId(), record.displayName(), sessionRegistry.count(), Instant.now()));
  }
}
