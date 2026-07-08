package com.learning.websocket.server.chat;

import java.time.Clock;
import java.time.Instant;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.learning.websocket.server.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatSocketEndpoint {

  private static final String LOG_CHAT_MESSAGE_PUBLISHED = "log.websocket.chat.message.published";

  private final LogMessages logMessages;
  private final ChatMessageEventPublisher chatMessageEventPublisher;

  public ChatSocketEndpoint(LogMessages logMessages, ChatMessageEventPublisher chatMessageEventPublisher) {
    this.logMessages = logMessages;
    this.chatMessageEventPublisher = chatMessageEventPublisher;
  }

  @MessageMapping("/chat.send")
  @SendTo("/topic/messages")
  public ChatMessage publish(@Payload ChatMessage message) {
    log.debug(logMessages.get(LOG_CHAT_MESSAGE_PUBLISHED, message.sender()));

    chatMessageEventPublisher.publishChatMessageEvent(message.sender(), message.content());

    return new ChatMessage(message.sender(), message.content(), Instant.now(Clock.systemUTC()));
  }

}
