package com.learning.websocket.lifecycle.session;

import java.time.Instant;

record SessionRecord(String sessionId, String displayName, Instant connectedAt) {
}
