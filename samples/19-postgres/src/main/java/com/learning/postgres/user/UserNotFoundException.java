package com.learning.postgres.user;

public class UserNotFoundException extends RuntimeException {

  private static final String MESSAGE_KEY = "error.user.not.found";

  private final transient Object[] args;

  public UserNotFoundException(Long id) {
    super(MESSAGE_KEY);

    this.args = new Object[] { id };
  }

  public String getMessageKey() {
    return MESSAGE_KEY;
  }

  public Object[] getArgs() {
    return args;
  }
}
