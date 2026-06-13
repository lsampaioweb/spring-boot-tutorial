package com.learning.websocket.server.chat;

import java.time.Instant;

public record ChatMessage(String sender, String content, Instant sentAt) {
}
