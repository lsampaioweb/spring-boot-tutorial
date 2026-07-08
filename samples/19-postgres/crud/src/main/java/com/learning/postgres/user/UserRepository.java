package com.learning.postgres.user;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.learning.postgres.db.DatabaseException;
import com.learning.postgres.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

/**
 * User repository for database operations using Spring JDBC.
 */
@Slf4j
@Repository
class UserRepository {

  private static final String LOG_USER_FETCHING_ALL = "log.user.fetching.all";
  private static final String LOG_USER_FETCHING_ID = "log.user.fetching.id";
  private static final String LOG_USER_INSERTING = "log.user.inserting";
  private static final String ERROR_USER_INSERT = "error.user.insert";
  private static final String LOG_USER_UPDATING = "log.user.updating";
  private static final String ERROR_USER_UPDATE = "error.user.update";
  private static final String LOG_USER_DELETING = "log.user.deleting";
  private static final String ERROR_USER_DELETE = "error.user.delete";

  private final JdbcClient jdbcClient;
  private final LogMessages logMessages;
  private final UserSqlConfigurationProperties sqlProperties;

  UserRepository(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      UserSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  /**
   * Retrieve all users.
   */
  List<User> findAll() {
    log.info(logMessages.get(LOG_USER_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAll())
        .query(User.class)
        .list();
  }

  /**
   * Retrieve a user by ID.
   */
  User findById(Long id) {
    log.info(logMessages.get(LOG_USER_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(UserSqlColumns.ID, id)
        .query(User.class)
        .optional()
        .orElse(null);
  }

  /**
   * Insert a new user; returns rows affected (1 on success).
   */
  int insert(User user) {
    log.info(logMessages.get(LOG_USER_INSERTING));
    try {
      return jdbcClient
          .sql(sqlProperties.insert())
          .param(UserSqlColumns.NAME, user.name())
          .param(UserSqlColumns.EMAIL, user.email())
          .update();
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_INSERT, e);
    }
  }

  /**
   * Update an existing user; returns rows affected.
   */
  int update(User user) {
    log.info(logMessages.get(LOG_USER_UPDATING, user.id()));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.update())
          .param(UserSqlColumns.ID, user.id())
          .param(UserSqlColumns.NAME, user.name())
          .param(UserSqlColumns.EMAIL, user.email())
          .update();

      if (rowsAffected == 0) {
        throw new UserNotFoundException(user.id());
      }
      return rowsAffected;
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_UPDATE, e);
    }
  }

  /**
   * Delete a user by ID; returns rows affected.
   */
  int deleteById(Long id) {
    log.info(logMessages.get(LOG_USER_DELETING, id));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.deleteById())
          .param(UserSqlColumns.ID, id)
          .update();

      if (rowsAffected == 0) {
        throw new UserNotFoundException(id);
      }
      return rowsAffected;
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_DELETE, e);
    }
  }
}
