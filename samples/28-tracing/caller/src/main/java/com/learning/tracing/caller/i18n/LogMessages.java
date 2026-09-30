package com.learning.tracing.caller.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Resolves developer-facing log messages in a fixed locale.
 */
@Component
public class LogMessages {

  private static final Locale LOG_LOCALE = Locale.ENGLISH;

  private final MessageSource messageSource;

  public LogMessages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Resolves a logging message key in the fixed logging locale.
   *
   * @param key message key
   * @param args format arguments
   * @return resolved message
   */
  public String get(String key, Object... args) {
    return messageSource.getMessage(key, args, LOG_LOCALE);
  }
}
