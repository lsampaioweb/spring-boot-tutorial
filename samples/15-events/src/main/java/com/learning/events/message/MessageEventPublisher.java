package com.learning.events.message;

import java.time.Clock;
import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class MessageEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  MessageEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  void publish(String sender, String content) {
    int contentLength = content != null ? content.length() : 0;

    eventPublisher.publishEvent(
        new MessagePublishedEvent(sender, contentLength, Instant.now(Clock.systemUTC())));
  }
}
