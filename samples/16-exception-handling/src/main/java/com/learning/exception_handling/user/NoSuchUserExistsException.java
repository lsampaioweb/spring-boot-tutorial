package com.learning.exception_handling.user;

public class NoSuchUserExistsException extends RuntimeException {

  public NoSuchUserExistsException(String message) {
    super(message);
  }

}
