package com.learning.events.message;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class MessageAuditListener {

  private final MessageSource messageSource;

  MessageAuditListener(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Async
  @EventListener
  void onMessagePublished(MessagePublishedEvent event) {
    log.info(
        messageSource.getMessage(
            "log.audit.recorded",
            new Object[] { event.sender(), event.contentLength(), event.publishedAt() },
            Locale.ENGLISH));
  }
}
