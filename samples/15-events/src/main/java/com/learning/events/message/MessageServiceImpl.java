package com.learning.events.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
class MessageServiceImpl implements MessageService {

  private final MessageEventPublisher messageEventPublisher;
  private final MessageSource messageSource;

  MessageServiceImpl(MessageEventPublisher messageEventPublisher, MessageSource messageSource) {
    this.messageEventPublisher = messageEventPublisher;
    this.messageSource = messageSource;
  }

  @Override
  public MessageResponse publish(MessageRequest request) {
    messageEventPublisher.publish(request.sender(), request.content());

    return new MessageResponse(
        request.sender(),
        messageSource.getMessage("message.status.accepted", null, LocaleContextHolder.getLocale()));
  }
}
