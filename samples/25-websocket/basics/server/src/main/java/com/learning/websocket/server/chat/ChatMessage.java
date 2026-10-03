package com.learning.websocket.server.chat;

import java.time.Instant;

/**
 * Represents a chat message; the server sets {@code sentAt} on the published
 * response.
 */
public record ChatMessage(String sender, String content, Instant sentAt) {
}
