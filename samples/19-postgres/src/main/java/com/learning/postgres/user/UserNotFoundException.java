package com.learning.postgres.user;

import com.learning.postgres.i18n.MessageSourceHolder;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(Long id) {
    super(MessageSourceHolder.getMessage("error.user.not.found", id));
  }
}
