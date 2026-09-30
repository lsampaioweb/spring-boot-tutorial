package com.learning.redis.cache.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;

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
