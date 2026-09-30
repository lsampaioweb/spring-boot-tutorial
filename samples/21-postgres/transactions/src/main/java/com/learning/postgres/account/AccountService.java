package com.learning.postgres.account;

interface AccountService {

  AccountResponse findById(Long id);

  TransferResponse transfer(TransferRequest request);
}
