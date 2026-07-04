package com.learning.postgres.db;

import org.springframework.http.HttpStatus;

import com.learning.postgres.exception.AppException;

public class DatabaseException extends AppException {

  public DatabaseException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, args, HttpStatus.INTERNAL_SERVER_ERROR, cause);
  }
}
