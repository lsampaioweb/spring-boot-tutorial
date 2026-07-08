package com.learning.geography.country;

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
class CountryRepository {

  private static final String LOG_COUNTRY_FETCHING_ALL = "log.country.fetching.all";
  private static final String LOG_COUNTRY_FETCHING_ID = "log.country.fetching.id";
  private static final String LOG_COUNTRY_INSERTING = "log.country.inserting";
  private static final String ERROR_COUNTRY_INSERT = "error.country.insert";
  private static final String LOG_COUNTRY_UPDATING = "log.country.updating";
  private static final String ERROR_COUNTRY_UPDATE = "error.country.update";
  private static final String LOG_COUNTRY_DELETING = "log.country.deleting";
  private static final String ERROR_COUNTRY_DELETE = "error.country.delete";

  private final JdbcClient jdbcClient;
  private final LogMessages logMessages;
  private final CountrySqlConfigurationProperties sqlProperties;

  CountryRepository(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      CountrySqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  List<Country> findAll() {
    log.info(logMessages.get(LOG_COUNTRY_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAll())
        .query(Country.class)
        .list();
  }

  Country findById(Long id) {
    log.info(logMessages.get(LOG_COUNTRY_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(CountrySqlColumns.ID, id)
        .query(Country.class)
        .optional()
        .orElse(null);
  }

  Country insert(Country country) {
    log.info(logMessages.get(LOG_COUNTRY_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(CountrySqlColumns.NAME, country.name())
          .param(CountrySqlColumns.ISO_CODE, country.isoCode())
          .update(keyHolder);
      Long id = keyHolder.getKey().longValue();
      return new Country(id, country.name(), country.isoCode());
    } catch (Exception e) {
      throw new DatabaseException(ERROR_COUNTRY_INSERT, e);
    }
  }

  int update(Country country) {
    log.info(logMessages.get(LOG_COUNTRY_UPDATING, country.id()));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.update())
          .param(CountrySqlColumns.ID, country.id())
          .param(CountrySqlColumns.NAME, country.name())
          .param(CountrySqlColumns.ISO_CODE, country.isoCode())
          .update();

      if (rowsAffected == 0) {
        throw new CountryNotFoundException(country.id());
      }
      return rowsAffected;
    } catch (CountryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_COUNTRY_UPDATE, e);
    }
  }

  int deleteById(Long id) {
    log.info(logMessages.get(LOG_COUNTRY_DELETING, id));
    try {
      int rowsAffected = jdbcClient
          .sql(sqlProperties.deleteById())
          .param(CountrySqlColumns.ID, id)
          .update();

      if (rowsAffected == 0) {
        throw new CountryNotFoundException(id);
      }
      return rowsAffected;
    } catch (CountryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_COUNTRY_DELETE, e);
    }
  }
}
