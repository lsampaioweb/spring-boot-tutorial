package com.learning.postgres.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

record TransferRequest(
    @NotNull(message = "error.validation.from-account.required") Long fromAccountId,
    @NotNull(message = "error.validation.to-account.required") Long toAccountId,
    @NotNull(message = "error.validation.amount.required")
    @Positive(message = "error.validation.amount.positive") BigDecimal amount) {
}
