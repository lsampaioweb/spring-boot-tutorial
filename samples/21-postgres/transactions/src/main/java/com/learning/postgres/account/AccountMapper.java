package com.learning.postgres.account;

import org.springframework.stereotype.Component;

@Component
class AccountMapper {

  AccountResponse toResponse(Account account) {
    return new AccountResponse(account.id(), account.ownerName(), account.balance());
  }
}
