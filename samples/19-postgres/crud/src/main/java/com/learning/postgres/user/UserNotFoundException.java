package com.learning.postgres.user;

import org.springframework.http.HttpStatus;

import com.learning.postgres.exception.AppException;

class UserNotFoundException extends AppException {

  UserNotFoundException(Long id) {
    super("error.user.not.found", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
