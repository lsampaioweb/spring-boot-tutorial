package com.learning.postgres.account;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

import com.learning.postgres.exception.AppException;

class InsufficientBalanceException extends AppException {

  InsufficientBalanceException(Long id, BigDecimal amount, BigDecimal currentBalance) {
    super("error.account.insufficient.balance", new Object[] { id, amount, currentBalance }, HttpStatus.BAD_REQUEST);
  }
}
