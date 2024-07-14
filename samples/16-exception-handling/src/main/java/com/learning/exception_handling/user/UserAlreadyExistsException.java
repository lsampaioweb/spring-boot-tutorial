package com.learning.exception_handling.user;

import com.learning.exception_handling.core.exception.EntityAlreadyExistsException;

public class UserAlreadyExistsException extends EntityAlreadyExistsException {

  public UserAlreadyExistsException(User entity) {
    super("user", new Object[] { entity.getName(), entity.getEmail() });
  }

}
