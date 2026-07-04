package com.learning.postgres.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import com.learning.postgres.db.DatabaseException;
import com.learning.postgres.i18n.LogMessages;

/**
 * User repository for batch database operations using Spring JDBC.
 */
@Slf4j
@Repository
class UserRepository {

  private static final String LOG_USER_BATCH_INSERTING = "log.user.batch.inserting";
  private static final String ERROR_USER_BATCH_INSERT = "error.user.batch.insert";

  private final JdbcClient jdbcClient;
  private final NamedParameterJdbcTemplate namedJdbcTemplate;
  private final LogMessages logMessages;
  private final String findAllSql;
  private final String batchInsertSql;

  UserRepository(
      JdbcClient jdbcClient,
      NamedParameterJdbcTemplate namedJdbcTemplate,
      LogMessages logMessages,
      @Value("${sql.users.find-all}") String findAllSql,
      @Value("${sql.users.insert}") String batchInsertSql) {
    this.jdbcClient = jdbcClient;
    this.namedJdbcTemplate = namedJdbcTemplate;
    this.logMessages = logMessages;
    this.findAllSql = findAllSql;
    this.batchInsertSql = batchInsertSql;
  }

  /**
   * Retrieve all users.
   */
  List<User> findAll() {
    return jdbcClient
        .sql(findAllSql)
        .query(User.class)
        .list();
  }

  /**
   * Batch insert users; returns array of rows affected per user.
   */
  int[] batchInsert(List<User> users) {
    log.info(logMessages.get(LOG_USER_BATCH_INSERTING, users.size()));
    try {
      List<Map<String, Object>> batchParams = users.stream()
          .map(user -> Map.<String, Object>of(
              UserSqlColumns.NAME, user.name(),
              UserSqlColumns.EMAIL, user.email()))
          .toList();

      SqlParameterSource[] batchArguments = batchParams.stream()
          .map(MapSqlParameterSource::new)
          .toArray(SqlParameterSource[]::new);

      return namedJdbcTemplate.batchUpdate(batchInsertSql, batchArguments);
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_BATCH_INSERT, e);
    }
  }
}
