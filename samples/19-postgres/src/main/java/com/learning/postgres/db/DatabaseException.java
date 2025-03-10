package com.learning.postgres.db;

import com.learning.postgres.i18n.MessageSourceHolder;

public class DatabaseException extends RuntimeException {
  public DatabaseException(String messageKey, Throwable cause, Object... args) {
    super(MessageSourceHolder.getMessage(messageKey, args), cause);
  }
}
