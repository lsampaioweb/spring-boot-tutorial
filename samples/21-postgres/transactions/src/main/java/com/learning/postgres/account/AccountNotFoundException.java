package com.learning.postgres.account;

import org.springframework.http.HttpStatus;

import com.learning.postgres.exception.AppException;

class AccountNotFoundException extends AppException {

  AccountNotFoundException(Long id) {
    super("error.account.not.found", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
