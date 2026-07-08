package com.learning.websocket.server.chat;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class ChatMessageAuditListener {

  private static final String LOG_WEBSOCKET_CHAT_AUDIT_RECORDED = "log.websocket.chat.audit.recorded";

  private final LogMessages logMessages;

  ChatMessageAuditListener(LogMessages logMessages) {
    this.logMessages = logMessages;
  }

  @EventListener
  void onChatMessagePublished(ChatMessagePublishedEvent event) {
    log.info(logMessages.get(
        LOG_WEBSOCKET_CHAT_AUDIT_RECORDED,
        event.sender(),
        event.contentLength(),
        event.localeTag(),
        event.publishedAt()));
  }
}
