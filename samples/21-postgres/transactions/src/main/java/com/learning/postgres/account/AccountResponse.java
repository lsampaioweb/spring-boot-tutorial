package com.learning.postgres.account;

import java.math.BigDecimal;

record AccountResponse(Long id, String ownerName, BigDecimal balance) {
}
