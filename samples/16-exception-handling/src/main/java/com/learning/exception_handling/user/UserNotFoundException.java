package com.learning.exception_handling.user;

import com.learning.exception_handling.core.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

  public UserNotFoundException(Long id) {
    super("user", new Object[] { id });
  }

}
