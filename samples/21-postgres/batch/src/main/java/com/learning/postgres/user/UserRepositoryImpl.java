package com.learning.postgres.user;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.learning.postgres.db.DatabaseException;

@Repository
class UserRepositoryImpl implements UserRepository {

  private static final String ERROR_USER_BATCH_INSERT = "error.user.batch.insert";

  private final JdbcClient jdbcClient;
  private final NamedParameterJdbcTemplate namedJdbcTemplate;
  private final UserSqlConfigurationProperties sqlProperties;

  UserRepositoryImpl(JdbcClient jdbcClient, NamedParameterJdbcTemplate namedJdbcTemplate,
      UserSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.namedJdbcTemplate = namedJdbcTemplate;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<User> findAll() {
    return jdbcClient.sql(sqlProperties.findAll()).query(User.class).list();
  }

  @Override
  public int[] batchInsert(List<User> users) {
    try {
      List<Map<String, Object>> batchParams = users.stream()
          .map(user -> Map.<String, Object>of(UserSqlColumns.NAME, user.name(),
              UserSqlColumns.EMAIL, user.email()))
          .toList();
      SqlParameterSource[] batchArguments = batchParams.stream()
          .map(MapSqlParameterSource::new).toArray(SqlParameterSource[]::new);
      return namedJdbcTemplate.batchUpdate(sqlProperties.insert(), batchArguments);
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_BATCH_INSERT, e);
    }
  }
}