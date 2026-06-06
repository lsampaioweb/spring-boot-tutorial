package com.learning.exception_handling.core.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

  private final String messageKey;
  private final Object[] args;
  private final HttpStatus status;

  protected CustomException(String messageKey, Object[] args, HttpStatus status) {
    super(messageKey);

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
