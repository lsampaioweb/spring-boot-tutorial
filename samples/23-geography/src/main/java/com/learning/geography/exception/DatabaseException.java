package com.learning.geography.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends AppException {

  public DatabaseException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, args, HttpStatus.INTERNAL_SERVER_ERROR, cause);
  }

}
