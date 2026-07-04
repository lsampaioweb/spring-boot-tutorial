package com.learning.postgres.account;

import java.math.BigDecimal;

record Account(Long id, String ownerName, BigDecimal balance) {
}
