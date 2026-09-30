package com.learning.postgres.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "{openapi.accounts.tag}")
class AccountRestController {

  private final AccountService accountService;

  AccountRestController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "{openapi.accounts.findById.summary}")
  public ResponseEntity<AccountResponse> findById(@PathVariable @Positive Long id) {
    return ResponseEntity.ok(accountService.findById(id));
  }

  @PostMapping("/transfer")
  @Operation(summary = "{openapi.accounts.transfer.summary}")
  public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
    return ResponseEntity.ok(accountService.transfer(request));
  }
}
