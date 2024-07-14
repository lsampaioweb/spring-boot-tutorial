package com.learning.exception_handling.core.exception;

public class EntityAlreadyExistsException extends CustomException {

  public EntityAlreadyExistsException(String prefix, Object[] objects) {
    super(getMessageKey(prefix), objects);
  }

  private static String getMessageKey(String prefix) {
    return String.format("%s.exists", prefix);
  }

}
