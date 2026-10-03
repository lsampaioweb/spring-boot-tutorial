package com.learning.websocket.server.chat;

import java.time.Instant;

/**
 * Carries message metadata for auditing without retaining or logging the
 * message body.
 */
record ChatMessagePublishedEvent(String sender, int contentLength, String localeTag, Instant publishedAt) {
}
