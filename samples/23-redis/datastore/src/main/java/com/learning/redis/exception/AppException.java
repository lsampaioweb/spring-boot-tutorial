package com.learning.redis.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;

/**
 * Abstract base for all domain exceptions in this application.
 * Subclasses provide the i18n message key, optional format args, and HTTP status.
 * Message text resolution happens in {@link GlobalExceptionHandler} via {@code MessageSource}.
 */
public abstract class AppException extends RuntimeException {

  private final String messageKey;
  private final String errorCode;
  private final transient Object[] args;
  private final HttpStatus status;

  protected AppException(String messageKey, HttpStatus status, Object... args) {
    super(messageKey);

    this.messageKey = messageKey;
    this.errorCode = toErrorCode(messageKey);
    this.status = status;
    this.args = args == null ? null : args.clone();
  }

  public String getMessageKey() {
    return messageKey;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public Object[] getArgs() {
    return args == null ? null : args.clone();
  }

  public HttpStatus getStatus() {
    return status;
  }

  private static String toErrorCode(String messageKey) {
    String trimmed = messageKey.startsWith("error.") ? messageKey.substring("error.".length()) : messageKey;

    return trimmed.replace('.', '_').toUpperCase(Locale.ROOT);
  }
}
