package com.learning.postgres.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {

  private final String messageKey;
  private final String errorCode;
  private final transient Object[] args;
  private final HttpStatus status;

  protected AppException(String messageKey, Object[] args, HttpStatus status) {
    this(messageKey, args, status, null);
  }

  protected AppException(String messageKey, Object[] args, HttpStatus status, Throwable cause) {
    super(messageKey, cause);

    this.messageKey = messageKey;
    this.errorCode = toErrorCode(messageKey);
    this.args = args;
    this.status = status;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public Object[] getArgs() {
    return args;
  }

  public HttpStatus getStatus() {
    return status;
  }

  private static String toErrorCode(String messageKey) {
    String trimmed = messageKey.startsWith("error.") ? messageKey.substring("error.".length()) : messageKey;

    return trimmed.replace('.', '_').toUpperCase(Locale.ROOT);
  }
}
