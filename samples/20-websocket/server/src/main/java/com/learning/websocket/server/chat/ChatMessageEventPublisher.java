package com.learning.websocket.server.chat;

import java.time.Clock;
import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
class ChatMessageEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  ChatMessageEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  void publishChatMessageEvent(String sender, String content) {
    String localeTag = LocaleContextHolder.getLocale().toLanguageTag();
    int contentLength = content != null ? content.length() : 0;

    eventPublisher.publishEvent(
        new ChatMessagePublishedEvent(sender, contentLength, localeTag, Instant.now(Clock.systemUTC())));
  }
}
