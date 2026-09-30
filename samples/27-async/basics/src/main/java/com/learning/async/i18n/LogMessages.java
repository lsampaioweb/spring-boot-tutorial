package com.learning.async.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LogMessages {

  private final MessageSource messageSource;

  LogMessages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /** Resolves a developer-facing message using the fixed English locale. */
  public String get(String key, Object... args) {
    return messageSource.getMessage(key, args, Locale.ENGLISH);
  }
}