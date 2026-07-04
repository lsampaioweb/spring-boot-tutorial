package com.learning.postgres.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * i18n utility for developer-facing English log messages.
 * Provides dual-method interface: ENGLISH logs for developers, locale-aware HTTP responses.
 */
@Component
public class LogMessages {

  private final MessageSource messageSource;

  LogMessages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Get message in English (for logging).
   */
  public String get(String key, Object... args) {
    return get(Locale.ENGLISH, key, args);
  }

  /**
   * Get message in specified locale (for HTTP responses).
   */
  public String get(Locale locale, String key, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }
}
