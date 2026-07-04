package com.learning.postgres.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.learning.postgres.i18n.LogMessages;

@Slf4j
@Service
class AccountServiceImpl implements AccountService {

  private static final String LOG_TRANSFER_STARTED = "log.transfer.started";
  private static final String LOG_TRANSFER_COMPLETED = "log.transfer.completed";
  private static final String LOG_ACCOUNT_FINDING = "log.account.finding";

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final LogMessages logMessages;

  AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, LogMessages logMessages) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public AccountResponse findById(Long id) {
    log.info(logMessages.get(LOG_ACCOUNT_FINDING, id));

    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new AccountNotFoundException(id));

    return accountMapper.toResponse(account);
  }

  @Override
  @Transactional
  public TransferResponse transfer(TransferRequest request) {
    log.info(logMessages.get(LOG_TRANSFER_STARTED, request.fromAccountId(), request.toAccountId(), request.amount()));

    Account fromAccount = accountRepository.findById(request.fromAccountId())
        .orElseThrow(() -> new AccountNotFoundException(request.fromAccountId()));

    accountRepository.findById(request.toAccountId())
        .orElseThrow(() -> new AccountNotFoundException(request.toAccountId()));

    if (fromAccount.balance().compareTo(request.amount()) < 0) {
      throw new InsufficientBalanceException(fromAccount.id(), request.amount(), fromAccount.balance());
    }

    int updatedRows = accountRepository.decreaseBalance(request.fromAccountId(), request.amount());
    if (updatedRows == 0) {
      throw new InsufficientBalanceException(fromAccount.id(), request.amount(), fromAccount.balance());
    }

    int increasedRows = accountRepository.increaseBalance(request.toAccountId(), request.amount());
    if (increasedRows == 0) {
      throw new AccountNotFoundException(request.toAccountId());
    }

    Account updatedFromAccount = accountRepository.findById(request.fromAccountId())
        .orElseThrow(() -> new AccountNotFoundException(request.fromAccountId()));

    Account updatedToAccount = accountRepository.findById(request.toAccountId())
        .orElseThrow(() -> new AccountNotFoundException(request.toAccountId()));

    log.info(logMessages.get(
        LOG_TRANSFER_COMPLETED,
        request.fromAccountId(),
        request.toAccountId(),
        updatedFromAccount.balance(),
        updatedToAccount.balance()));

    return new TransferResponse(
        request.fromAccountId(),
        request.toAccountId(),
        request.amount(),
        updatedFromAccount.balance(),
        updatedToAccount.balance());
  }
}
