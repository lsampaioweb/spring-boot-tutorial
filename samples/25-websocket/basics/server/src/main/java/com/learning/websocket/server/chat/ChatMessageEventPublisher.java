package com.learning.websocket.server.chat;

import java.time.Clock;
import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Publishes audit metadata for chat messages without including their contents.
 */
@Component
class ChatMessageEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  ChatMessageEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  void publishChatMessageEvent(String sender, String content) {
    // Capture the request locale and body length, but never copy the body into the
    // audit event.
    String localeTag = LocaleContextHolder.getLocale().toLanguageTag();
    int contentLength = content != null ? content.length() : 0;

    eventPublisher.publishEvent(
        new ChatMessagePublishedEvent(sender, contentLength, localeTag, Instant.now(Clock.systemUTC())));
  }
}
