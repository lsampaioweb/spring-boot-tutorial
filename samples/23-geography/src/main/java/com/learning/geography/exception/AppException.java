package com.learning.geography.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {

  private final String messageKey;
  private final transient Object[] args;
  private final HttpStatus status;

  protected AppException(String messageKey, Object[] args, HttpStatus status) {
    super(messageKey);

    this.messageKey = messageKey;
    this.args = args;
    this.status = status;
  }

  protected AppException(String messageKey, Object[] args, HttpStatus status, Throwable cause) {
    super(messageKey, cause);

    this.messageKey = messageKey;
    this.args = args;
    this.status = status;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public Object[] getArgs() {
    return args;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
