package com.learning.postgres.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learning.postgres.db.DatabaseException;
import com.learning.postgres.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class UserRepository {

  private static final String LOG_USER_FETCHING_ALL = "log.user.fetching.all";
  private static final String LOG_USER_FETCHING_ID = "log.user.fetching.id";
  private static final String ERROR_USER_NOT_FOUND = "error.user.not.found";
  private static final String LOG_USER_INSERTING = "log.user.inserting";
  private static final String LOG_USER_INSERTING_BATCH = "log.user.inserting.batch";
  private static final String ERROR_USER_INSERT = "error.user.insert";
  private static final String ERROR_USER_INSERT_BATCH = "error.user.insert.batch";
  private static final String LOG_USER_DELETING_ID = "log.user.deleting.id";

  private final JdbcClient jdbcClient;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final LogMessages logMessages;
  private final String findAllSql;
  private final String findByIdSql;
  private final String insertSql;
  private final String deleteByIdSql;

  UserRepository(
      JdbcClient jdbcClient,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      LogMessages logMessages,
      @Value("${sql.users.find-all}") String findAllSql,
      @Value("${sql.users.find-by-id}") String findByIdSql,
      @Value("${sql.users.insert}") String insertSql,
      @Value("${sql.users.delete-by-id}") String deleteByIdSql) {
    this.jdbcClient = jdbcClient;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.logMessages = logMessages;
    this.findAllSql = findAllSql;
    this.findByIdSql = findByIdSql;
    this.insertSql = insertSql;
    this.deleteByIdSql = deleteByIdSql;
  }

  List<Model> findAll() {
    log.info(logMessages.get(LOG_USER_FETCHING_ALL));

    return jdbcClient
        .sql(findAllSql)
        .query(Model.class)
        .list();
  }

  Model findById(Long id) {
    log.info(logMessages.get(LOG_USER_FETCHING_ID, id));

    return jdbcClient
        .sql(findByIdSql)
        .param("id", id)
        .query(Model.class)
        .optional()
        .orElseThrow(() -> {
          log.warn(logMessages.get(ERROR_USER_NOT_FOUND, id));

          return new UserNotFoundException(id);
        });
  }

  void insert(Model model) {
    log.info(logMessages.get(LOG_USER_INSERTING));

    try {
      jdbcClient
          .sql(insertSql)
          .param("name", model.getName())
          .param("email", model.getEmail())
          .update();
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_INSERT, e);
    }
  }

  @Transactional
  void insertAll(List<Model> list) {
    log.info(logMessages.get(LOG_USER_INSERTING_BATCH, list.size()));

    try {
      namedParameterJdbcTemplate.batchUpdate(insertSql, SqlParameterSourceUtils.createBatch(list));
    } catch (Exception e) {
      log.error(logMessages.get(ERROR_USER_INSERT_BATCH), e);

      throw new DatabaseException(ERROR_USER_INSERT_BATCH, e);
    }
  }

  void deleteById(Long id) {
    log.info(logMessages.get(LOG_USER_DELETING_ID, id));

    int rowsAffected = jdbcClient
        .sql(deleteByIdSql)
        .param("id", id)
        .update();

    if (rowsAffected == 0) {
      throw new UserNotFoundException(id);
    }
  }
}
