package com.learning.async.core.exception;

import org.springframework.http.HttpStatus;

/** Carries a localized API error contract from application code. */
public abstract class AppException extends RuntimeException {

  private final String messageKey;
  private final String errorCode;
  private final transient Object[] args;
  private final HttpStatus status;

  /** Creates an API exception with its localized message and response status. */
  protected AppException(String messageKey, String errorCode, Object[] args, HttpStatus status) {
    this(messageKey, errorCode, args, status, null);
  }

  /** Creates an API exception while preserving its underlying cause. */
  protected AppException(
      String messageKey,
      String errorCode,
      Object[] args,
      HttpStatus status,
      Throwable cause) {
    super(messageKey, cause);
    this.messageKey = messageKey;
    this.errorCode = errorCode;
    this.args = args;
    this.status = status;
  }

  /** Returns the message bundle key. */
  public String getMessageKey() {
    return messageKey;
  }

  /** Returns the stable API error code. */
  public String getErrorCode() {
    return errorCode;
  }

  /** Returns message formatting arguments. */
  public Object[] getArgs() {
    return args;
  }

  /** Returns the HTTP status for this error. */
  public HttpStatus getStatus() {
    return status;
  }
}