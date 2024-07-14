package com.learning.exception_handling.core.exception;

public class EntityNotFoundException extends CustomException {

  public EntityNotFoundException(String prefix, Object[] objects) {
    super(getMessageKey(prefix), objects);
  }

  private static String getMessageKey(String prefix) {
    return String.format("%s.notfound", prefix);
  }

}
