package com.learning.postgres.account;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts")
class AccountController {

  private final AccountService accountService;

  AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.findById(id));
  }

  @PostMapping("/transfer")
  public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.transfer(request));
  }
}
