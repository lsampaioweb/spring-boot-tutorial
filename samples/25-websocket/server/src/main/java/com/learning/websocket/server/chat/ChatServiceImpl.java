package com.learning.websocket.server.chat;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Service;

/**
 * Publishes chat messages and delegates connection counts to the session
 * tracker.
 */
@Service
class ChatServiceImpl implements ChatService {

  private final ChatMessageEventPublisher eventPublisher;
  private final ConnectionTracker connectionTracker;

  ChatServiceImpl(ChatMessageEventPublisher eventPublisher, ConnectionTracker connectionTracker) {
    this.eventPublisher = eventPublisher;
    this.connectionTracker = connectionTracker;
  }

  @Override
  public ChatMessage publish(ChatMessage message) {
    // Record audit metadata before returning the broadcast payload with a server
    // timestamp.
    eventPublisher.publishChatMessageEvent(message.sender(), message.content());
    return new ChatMessage(message.sender(), message.content(), Instant.now(Clock.systemUTC()));
  }

  @Override
  public int openConnections() {
    return connectionTracker.getOpenConnections();
  }
}