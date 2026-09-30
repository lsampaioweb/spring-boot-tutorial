package com.learning.websocket.server.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

/** Exposes the current WebSocket chat connection count over HTTP. */
@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
class ChatRestController {

  private static final String LOG_WEBSOCKET_STATUS_REQUESTED = "log.websocket.status.requested";

  private final ChatService chatService;
  private final LogMessages logMessages;

  ChatRestController(ChatService chatService, LogMessages logMessages) {
    this.chatService = chatService;
    this.logMessages = logMessages;
  }

  /** Returns the number of currently connected chat sessions. */
  @GetMapping("/connections")
  ChatConnectionStatusResponse openConnections() {
    log.debug(logMessages.get(LOG_WEBSOCKET_STATUS_REQUESTED, chatService.openConnections()));
    return new ChatConnectionStatusResponse(chatService.openConnections());
  }
}
