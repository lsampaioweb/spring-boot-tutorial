package com.learning.websocket.lifecycle.session;

import org.springframework.stereotype.Component;

@Component
class SessionMapper {

  SessionResponse toResponse(SessionRecord record) {
    return new SessionResponse(record.sessionId(), record.displayName(), record.connectedAt());
  }
}
