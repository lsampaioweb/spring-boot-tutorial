package com.learning.postgres.account;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sql.accounts")
record AccountSqlConfigurationProperties(
    String findById,
    String decreaseBalance,
    String increaseBalance) {
}
