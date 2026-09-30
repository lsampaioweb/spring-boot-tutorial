package com.learning.postgres.account;

import java.math.BigDecimal;
import java.util.Optional;

interface AccountRepository {
  Optional<Account> findById(Long id);

  int decreaseBalance(Long id, BigDecimal amount);

  int increaseBalance(Long id, BigDecimal amount);
}
