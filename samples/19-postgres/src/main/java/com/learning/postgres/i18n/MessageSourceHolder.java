package com.learning.postgres.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceHolder {
  private final MessageSource messageSource;

  MessageSourceHolder(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public @NonNull String getMessage(String key, Object... args) {
    return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
