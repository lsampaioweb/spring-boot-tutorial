package com.learning.websocket.server.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/** Resolves developer-facing log messages using English by default. */
@Component
public class LogMessages {

  private final MessageSource messageSource;

  LogMessages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /** Resolves a log message in English with the supplied arguments. */
  public String get(String key, Object... args) {
    return get(Locale.ENGLISH, key, args);
  }

  /** Resolves a log message in the supplied locale. */
  public String get(Locale locale, String key, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }

}
