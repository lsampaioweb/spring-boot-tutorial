package com.learning.exception_handling.core.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends CustomException {

  public EntityNotFoundException(String prefix, Object[] objects) {
    super(buildKey(prefix), objects, HttpStatus.NOT_FOUND);
  }

  private static String buildKey(String prefix) {
    return String.format("%s.notfound", prefix);
  }

}
