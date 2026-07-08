package com.learning.postgres.account;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class AccountRepository {

  private final JdbcClient jdbcClient;
  private final AccountSqlConfigurationProperties sqlProperties;

  AccountRepository(
      JdbcClient jdbcClient,
      AccountSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.sqlProperties = sqlProperties;
  }

  Optional<Account> findById(Long id) {
    return jdbcClient
        .sql(sqlProperties.findById())
        .param("id", id)
        .query(Account.class)
        .optional();
  }

  int decreaseBalance(Long id, BigDecimal amount) {
    return jdbcClient
        .sql(sqlProperties.decreaseBalance())
        .param("id", id)
        .param("amount", amount)
        .update();
  }

  int increaseBalance(Long id, BigDecimal amount) {
    return jdbcClient
        .sql(sqlProperties.increaseBalance())
        .param("id", id)
        .param("amount", amount)
        .update();
  }
}
