package com.learning.websocket.server.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

/**
 * Accepts client messages at {@code /app/chat.send} and broadcasts results to
 * {@code /topic/messages}.
 */
@Controller
@Slf4j
public class ChatSocketController {

  private static final String LOG_CHAT_MESSAGE_PUBLISHED = "log.websocket.chat.message.published";

  private final LogMessages logMessages;
  private final ChatService chatService;

  /** Creates the socket controller with its service and log message resolver. */
  public ChatSocketController(LogMessages logMessages, ChatService chatService) {
    this.logMessages = logMessages;
    this.chatService = chatService;
  }

  /**
   * Delegates a client message to the service and returns it to all topic
   * subscribers.
   */
  @MessageMapping("/chat.send")
  @SendTo("/topic/messages")
  public ChatMessage publish(@Payload ChatMessage message) {
    log.debug(logMessages.get(LOG_CHAT_MESSAGE_PUBLISHED, message.sender()));
    return chatService.publish(message);
  }
}