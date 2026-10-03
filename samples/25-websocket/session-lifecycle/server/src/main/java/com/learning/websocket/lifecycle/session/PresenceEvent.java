package com.learning.websocket.lifecycle.session;

import java.time.Instant;

public record PresenceEvent(
    PresenceType type,
    String sessionId,
    String displayName,
    int activeCount,
    Instant at) {
}
