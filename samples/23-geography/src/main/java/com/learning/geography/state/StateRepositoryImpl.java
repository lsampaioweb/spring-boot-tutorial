package com.learning.geography.state;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.learning.geography.exception.DatabaseException;

@Repository
class StateRepositoryImpl implements StateRepository {

  private static final String ERROR_STATE_INSERT = "error.state.insert";
  private static final String ERROR_STATE_UPDATE = "error.state.update";
  private static final String ERROR_STATE_DELETE = "error.state.delete";

  private final JdbcClient jdbcClient;
  private final StateSqlConfigurationProperties sqlProperties;

  StateRepositoryImpl(
      JdbcClient jdbcClient,
      StateSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<State> findAll(int limit, int offset) {
    return jdbcClient
        .sql(sqlProperties.findAllPaged())
        .param("limit", limit)
        .param("offset", offset)
        .query(State.class)
        .list();
  }

  @Override
  public long countAll() {
    return jdbcClient
        .sql(sqlProperties.countAll())
        .query(Integer.class)
        .single();
  }

  @Override
  public State findById(Integer id) {
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(StateSqlColumns.ID, id)
        .query(State.class)
        .optional()
        .orElse(null);
  }

  @Override
  public State insert(State state) {
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
