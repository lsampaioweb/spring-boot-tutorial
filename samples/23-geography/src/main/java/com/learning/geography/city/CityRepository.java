package com.learning.geography.city;

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
class CityRepository {

  private static final String LOG_CITY_FETCHING_ALL = "log.city.fetching.all";
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

  CityRepository(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      CitySqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  List<City> findAll() {
    log.info(logMessages.get(LOG_CITY_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAll())
        .query(City.class)
        .list();
  }

  City findById(Long id) {
    log.info(logMessages.get(LOG_CITY_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(CitySqlColumns.ID, id)
        .query(City.class)
        .optional()
        .orElse(null);
  }

  City insert(City city) {
    log.info(logMessages.get(LOG_CITY_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(CitySqlColumns.STATE_ID, city.stateId())
          .param(CitySqlColumns.NAME, city.name())
          .update(keyHolder);
      Long id = keyHolder.getKey().longValue();
      return new City(id, city.stateId(), city.name());
    } catch (Exception e) {
      throw new DatabaseException(ERROR_CITY_INSERT, e);
    }
  }

  int update(City city) {
    log.info(logMessages.get(LOG_CITY_UPDATING, city.id()));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.update())
          .param(CitySqlColumns.ID, city.id())
          .param(CitySqlColumns.STATE_ID, city.stateId())
          .param(CitySqlColumns.NAME, city.name())
          .update();

      if (rowsAffected == 0) {
        throw new CityNotFoundException(city.id());
      }
      return rowsAffected;
    } catch (CityNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_CITY_UPDATE, e);
    }
  }

  int deleteById(Long id) {
    log.info(logMessages.get(LOG_CITY_DELETING, id));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.deleteById())
          .param(CitySqlColumns.ID, id)
          .update();

      if (rowsAffected == 0) {
        throw new CityNotFoundException(id);
      }
      return rowsAffected;
    } catch (CityNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_CITY_DELETE, e);
    }
  }
}
