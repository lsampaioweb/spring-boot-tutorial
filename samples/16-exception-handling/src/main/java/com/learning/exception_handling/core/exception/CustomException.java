package com.learning.exception_handling.core.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.learning.exception_handling.core.i18n.MessageSourceHolder;

public abstract class CustomException extends RuntimeException {

  public CustomException(String messageKey, Object[] objects) {
    super(getMessage(messageKey, objects));
  }

  private static String getMessage(String messageKey, Object[] objects) {
    return getMessageSource().getMessage(messageKey, objects, getLocale());
  }

  private static MessageSource getMessageSource() {
    return MessageSourceHolder.getMessageSource();
  }

  private static Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }
}
