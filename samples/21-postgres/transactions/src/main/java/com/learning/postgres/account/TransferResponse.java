package com.learning.postgres.account;

import java.math.BigDecimal;

record TransferResponse(
    Long fromAccountId,
    Long toAccountId,
    BigDecimal amount,
    BigDecimal fromAccountBalance,
    BigDecimal toAccountBalance) {
}
