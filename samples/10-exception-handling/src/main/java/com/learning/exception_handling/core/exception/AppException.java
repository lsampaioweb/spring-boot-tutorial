package com.learning.exception_handling.core.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {

  private final String messageKey;
  private final String errorCode;
  private final transient Object[] args;
  private final HttpStatus status;

  protected AppException(String messageKey, String errorCode, Object[] args, HttpStatus status) {
    super(messageKey);

    this.messageKey = messageKey;
    this.errorCode = errorCode;
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
}
