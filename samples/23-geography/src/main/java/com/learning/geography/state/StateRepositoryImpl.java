package com.learning.geography.state;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.learning.geography.exception.DatabaseException;
import com.learning.geography.i18n.LogMessages;

@Slf4j
@Repository
class StateRepositoryImpl implements StateRepository {

  private static final String LOG_STATE_FETCHING_ALL = "log.state.fetching.all";
  private static final String LOG_STATE_COUNTING_ALL = "log.state.counting.all";
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

  StateRepositoryImpl(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      StateSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<State> findAll(int limit, int offset) {
    log.debug(logMessages.get(LOG_STATE_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAllPaged())
        .param("limit", limit)
        .param("offset", offset)
        .query(State.class)
        .list();
  }

  @Override
  public long countAll() {
    log.debug(logMessages.get(LOG_STATE_COUNTING_ALL));
    return jdbcClient
        .sql(sqlProperties.countAll())
        .query(Integer.class)
        .single();
  }

  @Override
  public State findById(Integer id) {
    log.debug(logMessages.get(LOG_STATE_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(StateSqlColumns.ID, id)
        .query(State.class)
        .optional()
        .orElse(null);
  }

  @Override
  public State insert(State state) {
    log.info(logMessages.get(LOG_STATE_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(StateSqlColumns.COUNTRY_ID, state.countryId())
          .param(StateSqlColumns.NAME, state.name())
          .param(StateSqlColumns.ABBREVIATION, state.abbreviation())
          .update(keyHolder);

      Number key = keyHolder.getKey();
      if (key == null) {
        throw new IllegalStateException("generated.key.missing");
      }
      Integer id = key.intValue();

      return new State(id, state.countryId(), state.name(), state.abbreviation());
    } catch (DataAccessException | IllegalStateException e) {
      throw new DatabaseException(ERROR_STATE_INSERT, e);
    }
  }

  @Override
  public int update(State state) {
    log.info(logMessages.get(LOG_STATE_UPDATING, state.id()));
    try {
      return jdbcClient
          .sql(sqlProperties.update())
          .param(StateSqlColumns.ID, state.id())
          .param(StateSqlColumns.COUNTRY_ID, state.countryId())
          .param(StateSqlColumns.NAME, state.name())
          .param(StateSqlColumns.ABBREVIATION, state.abbreviation())
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_STATE_UPDATE, e);
    }
  }

  @Override
  public int deleteById(Integer id) {
    log.info(logMessages.get(LOG_STATE_DELETING, id));
    try {
      return jdbcClient
          .sql(sqlProperties.deleteById())
          .param(StateSqlColumns.ID, id)
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_STATE_DELETE, e);
    }
  }
}
