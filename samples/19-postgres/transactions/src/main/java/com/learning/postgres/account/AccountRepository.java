package com.learning.postgres.account;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class AccountRepository {

  private final JdbcClient jdbcClient;
  private final String findByIdSql;
  private final String decreaseBalanceSql;
  private final String increaseBalanceSql;

  AccountRepository(
      JdbcClient jdbcClient,
      @Value("${sql.accounts.find-by-id}") String findByIdSql,
      @Value("${sql.accounts.decrease-balance}") String decreaseBalanceSql,
      @Value("${sql.accounts.increase-balance}") String increaseBalanceSql) {
    this.jdbcClient = jdbcClient;
    this.findByIdSql = findByIdSql;
    this.decreaseBalanceSql = decreaseBalanceSql;
    this.increaseBalanceSql = increaseBalanceSql;
  }

  Optional<Account> findById(Long id) {
    return jdbcClient
        .sql(findByIdSql)
        .param("id", id)
        .query(Account.class)
        .optional();
  }

  int decreaseBalance(Long id, BigDecimal amount) {
    return jdbcClient
        .sql(decreaseBalanceSql)
        .param("id", id)
        .param("amount", amount)
        .update();
  }

  int increaseBalance(Long id, BigDecimal amount) {
    return jdbcClient
        .sql(increaseBalanceSql)
        .param("id", id)
        .param("amount", amount)
        .update();
  }
}
