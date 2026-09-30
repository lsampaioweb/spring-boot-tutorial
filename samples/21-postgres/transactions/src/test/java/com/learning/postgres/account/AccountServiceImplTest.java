package com.learning.postgres.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learning.postgres.i18n.LogMessages;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private LogMessages logMessages;

  private AccountServiceImpl accountService;

  @BeforeEach
  void setUp() {
    accountService = new AccountServiceImpl(accountRepository, new AccountMapper(), logMessages);
  }

  @Test
  void transfer_whenSufficientBalance_shouldTransferSuccessfully() {
    TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("100.00"));
    Account from = new Account(1L, "Alice", new BigDecimal("1000.00"));
    Account to = new Account(2L, "Bob", new BigDecimal("500.00"));
    Account updatedFrom = new Account(1L, "Alice", new BigDecimal("900.00"));
    Account updatedTo = new Account(2L, "Bob", new BigDecimal("600.00"));

    doReturn(Optional.of(from)).doReturn(Optional.of(updatedFrom)).when(accountRepository).findById(1L);
    doReturn(Optional.of(to)).doReturn(Optional.of(updatedTo)).when(accountRepository).findById(2L);
    when(accountRepository.decreaseBalance(1L, new BigDecimal("100.00"))).thenReturn(1);
    when(accountRepository.increaseBalance(2L, new BigDecimal("100.00"))).thenReturn(1);

    TransferResponse response = accountService.transfer(request);

    assertThat(response.fromAccountId()).isEqualTo(1L);
    assertThat(response.toAccountId()).isEqualTo(2L);
    assertThat(response.amount()).isEqualByComparingTo("100.00");
    assertThat(response.fromAccountBalance()).isEqualByComparingTo("900.00");
    assertThat(response.toAccountBalance()).isEqualByComparingTo("600.00");
  }

  @Test
  void transfer_whenInsufficientBalance_shouldThrowException() {
    TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("1100.00"));
    Account from = new Account(1L, "Alice", new BigDecimal("1000.00"));
    Account to = new Account(2L, "Bob", new BigDecimal("500.00"));

    when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
    when(accountRepository.findById(2L)).thenReturn(Optional.of(to));

    assertThatThrownBy(() -> accountService.transfer(request)).isInstanceOf(InsufficientBalanceException.class);
  }

  @Test
  void findById_whenAccountExists_shouldReturnResponse() {
    Account account = new Account(1L, "Alice", new BigDecimal("1000.00"));

    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

    AccountResponse response = accountService.findById(1L);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.ownerName()).isEqualTo("Alice");
    assertThat(response.balance()).isEqualByComparingTo("1000.00");
  }

  @Test
  void findById_whenAccountMissing_shouldThrowException() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> accountService.findById(99L)).isInstanceOf(AccountNotFoundException.class);
  }
}
