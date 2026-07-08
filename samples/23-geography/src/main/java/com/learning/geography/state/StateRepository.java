package com.learning.geography.state;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.learning.geography.db.DatabaseException;
import com.learning.geography.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class StateRepository {

  private static final String LOG_STATE_FETCHING_ALL = "log.state.fetching.all";
  private static final String LOG_STATE_FETCHING_ID = "log.state.fetching.id";
  private static final String LOG_STATE_INSERTING = "log.state.inserting";
  private static final String ERROR_STATE_INSERT = "error.state.insert";
  private static final String LOG_STATE_UPDATING = "log.state.updating";
  private static final String ERROR_STATE_UPDATE = "error.state.update";
  private static final String LOG_STATE_DELETING = "log.state.deleting";
  private static final String ERROR_STATE_DELETE = "error.state.delete";

  private final JdbcClient jdbcClient;
  private final LogMessages logMessages;
  private final StateSqlConfigurationProperties sqlProperties;

  StateRepository(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      StateSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  List<State> findAll() {
    log.info(logMessages.get(LOG_STATE_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAll())
        .query(State.class)
        .list();
  }

  State findById(Long id) {
    log.info(logMessages.get(LOG_STATE_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(StateSqlColumns.ID, id)
        .query(State.class)
        .optional()
        .orElse(null);
  }

  State insert(State state) {
    log.info(logMessages.get(LOG_STATE_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(StateSqlColumns.COUNTRY_ID, state.countryId())
          .param(StateSqlColumns.NAME, state.name())
          .param(StateSqlColumns.ABBREVIATION, state.abbreviation())
          .update(keyHolder);
      Long id = keyHolder.getKey().longValue();
      return new State(id, state.countryId(), state.name(), state.abbreviation());
    } catch (Exception e) {
      throw new DatabaseException(ERROR_STATE_INSERT, e);
    }
  }

  int update(State state) {
    log.info(logMessages.get(LOG_STATE_UPDATING, state.id()));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.update())
          .param(StateSqlColumns.ID, state.id())
          .param(StateSqlColumns.COUNTRY_ID, state.countryId())
          .param(StateSqlColumns.NAME, state.name())
          .param(StateSqlColumns.ABBREVIATION, state.abbreviation())
          .update();

      if (rowsAffected == 0) {
        throw new StateNotFoundException(state.id());
      }
      return rowsAffected;
    } catch (StateNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_STATE_UPDATE, e);
    }
  }

  int deleteById(Long id) {
    log.info(logMessages.get(LOG_STATE_DELETING, id));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.deleteById())
          .param(StateSqlColumns.ID, id)
          .update();

      if (rowsAffected == 0) {
        throw new StateNotFoundException(id);
      }
      return rowsAffected;
    } catch (StateNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_STATE_DELETE, e);
    }
  }
}
