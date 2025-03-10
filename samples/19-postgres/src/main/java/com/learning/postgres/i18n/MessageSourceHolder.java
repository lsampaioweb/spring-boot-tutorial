package com.learning.postgres.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceHolder {
  private static MessageSource messageSource;

  public MessageSourceHolder(MessageSource messageSource) {
    MessageSourceHolder.messageSource = messageSource;
  }

  public static String getMessage(String key, Object... args) {
    return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
