package com.learning.vault.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LogMessages {

  private final MessageSource messageSource;

  public LogMessages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public String get(String key, Object... args) {
    return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
