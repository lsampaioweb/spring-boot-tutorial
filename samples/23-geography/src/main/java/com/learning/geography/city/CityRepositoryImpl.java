package com.learning.geography.city;

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
class CityRepositoryImpl implements CityRepository {

  private static final String LOG_CITY_FETCHING_ALL = "log.city.fetching.all";
  private static final String LOG_CITY_COUNTING_ALL = "log.city.counting.all";
  private static final String LOG_CITY_FETCHING_ID = "log.city.fetching.id";
  private static final String LOG_CITY_INSERTING = "log.city.inserting";
  private static final String ERROR_CITY_INSERT = "error.city.insert";
  private static final String LOG_CITY_UPDATING = "log.city.updating";
  private static final String ERROR_CITY_UPDATE = "error.city.update";
  private static final String LOG_CITY_DELETING = "log.city.deleting";
  private static final String ERROR_CITY_DELETE = "error.city.delete";

  private final JdbcClient jdbcClient;
  private final LogMessages logMessages;
  private final CitySqlConfigurationProperties sqlProperties;

  CityRepositoryImpl(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      CitySqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<City> findAll(int limit, int offset) {
    log.debug(logMessages.get(LOG_CITY_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAllPaged())
        .param("limit", limit)
        .param("offset", offset)
        .query(City.class)
        .list();
  }

  @Override
  public long countAll() {
    log.debug(logMessages.get(LOG_CITY_COUNTING_ALL));
    return jdbcClient
        .sql(sqlProperties.countAll())
        .query(Integer.class)
        .single();
  }

  @Override
  public City findById(Integer id) {
    log.debug(logMessages.get(LOG_CITY_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(CitySqlColumns.ID, id)
        .query(City.class)
        .optional()
        .orElse(null);
  }

  @Override
  public City insert(City city) {
    log.info(logMessages.get(LOG_CITY_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(CitySqlColumns.STATE_ID, city.stateId())
          .param(CitySqlColumns.NAME, city.name())
          .update(keyHolder);

      Number key = keyHolder.getKey();
      if (key == null) {
        throw new IllegalStateException("generated.key.missing");
      }
      Integer id = key.intValue();

      return new City(id, city.stateId(), city.name());
    } catch (DataAccessException | IllegalStateException e) {
      throw new DatabaseException(ERROR_CITY_INSERT, e);
    }
  }

  @Override
  public int update(City city) {
    log.info(logMessages.get(LOG_CITY_UPDATING, city.id()));
    try {
      return jdbcClient
          .sql(sqlProperties.update())
          .param(CitySqlColumns.ID, city.id())
          .param(CitySqlColumns.STATE_ID, city.stateId())
          .param(CitySqlColumns.NAME, city.name())
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_CITY_UPDATE, e);
    }
  }

  @Override
  public int deleteById(Integer id) {
    log.info(logMessages.get(LOG_CITY_DELETING, id));
    try {
      return jdbcClient
          .sql(sqlProperties.deleteById())
          .param(CitySqlColumns.ID, id)
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_CITY_DELETE, e);
    }
  }
}
