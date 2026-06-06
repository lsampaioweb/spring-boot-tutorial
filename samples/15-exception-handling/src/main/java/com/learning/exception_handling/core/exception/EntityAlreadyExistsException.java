package com.learning.exception_handling.core.exception;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends CustomException {

  public EntityAlreadyExistsException(String prefix, Object[] objects) {
    super(buildKey(prefix), objects, HttpStatus.CONFLICT);
  }

  private static String buildKey(String prefix) {
    return String.format("%s.exists", prefix);
  }

}
