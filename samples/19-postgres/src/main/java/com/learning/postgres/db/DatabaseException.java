package com.learning.postgres.db;

public class DatabaseException extends RuntimeException {

  private final String messageKey;
  private final Object[] args;

  public DatabaseException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, cause);

    this.messageKey = messageKey;
    this.args = args;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public Object[] getArgs() {
    return args;
  }
}
