package com.learning.geography.state;

import org.springframework.http.HttpStatus;

import com.learning.geography.exception.AppException;

class StateNotFoundException extends AppException {

  StateNotFoundException(Integer id) {
    super("error.state.not.found", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
