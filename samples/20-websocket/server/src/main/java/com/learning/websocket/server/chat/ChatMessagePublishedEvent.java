package com.learning.websocket.server.chat;

import java.time.Instant;

record ChatMessagePublishedEvent(String sender, int contentLength, String localeTag, Instant publishedAt) {
}
