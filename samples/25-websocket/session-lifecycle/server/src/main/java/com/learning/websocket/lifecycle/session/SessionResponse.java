package com.learning.websocket.lifecycle.session;

import java.time.Instant;

public record SessionResponse(String sessionId, String displayName, Instant connectedAt) {
}
