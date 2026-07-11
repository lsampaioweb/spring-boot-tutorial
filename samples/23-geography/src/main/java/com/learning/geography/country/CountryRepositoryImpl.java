package com.learning.geography.country;

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
class CountryRepositoryImpl implements CountryRepository {

  private static final String LOG_COUNTRY_FETCHING_ALL = "log.country.fetching.all";
  private static final String LOG_COUNTRY_COUNTING_ALL = "log.country.counting.all";
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

  CountryRepositoryImpl(
      JdbcClient jdbcClient,
      LogMessages logMessages,
      CountrySqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.logMessages = logMessages;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<Country> findAll(int limit, int offset) {
    log.debug(logMessages.get(LOG_COUNTRY_FETCHING_ALL));
    return jdbcClient
        .sql(sqlProperties.findAllPaged())
        .param("limit", limit)
        .param("offset", offset)
        .query(Country.class)
        .list();
  }

  @Override
  public long countAll() {
    log.debug(logMessages.get(LOG_COUNTRY_COUNTING_ALL));
    return jdbcClient
        .sql(sqlProperties.countAll())
        .query(Integer.class)
        .single();
  }

  @Override
  public Country findById(Integer id) {
    log.debug(logMessages.get(LOG_COUNTRY_FETCHING_ID, id));
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(CountrySqlColumns.ID, id)
        .query(Country.class)
        .optional()
        .orElse(null);
  }

  @Override
  public Country insert(Country country) {
    log.info(logMessages.get(LOG_COUNTRY_INSERTING));
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcClient
          .sql(sqlProperties.insert())
          .param(CountrySqlColumns.NAME, country.name())
          .param(CountrySqlColumns.ISO_CODE, country.isoCode())
          .update(keyHolder);

      Number key = keyHolder.getKey();
      if (key == null) {
        throw new IllegalStateException("generated.key.missing");
      }
      Integer id = key.intValue();

      return new Country(id, country.name(), country.isoCode());
    } catch (DataAccessException | IllegalStateException e) {
      throw new DatabaseException(ERROR_COUNTRY_INSERT, e);
    }
  }

  @Override
  public int update(Country country) {
    log.info(logMessages.get(LOG_COUNTRY_UPDATING, country.id()));
    try {
      return jdbcClient
          .sql(sqlProperties.update())
          .param(CountrySqlColumns.ID, country.id())
          .param(CountrySqlColumns.NAME, country.name())
          .param(CountrySqlColumns.ISO_CODE, country.isoCode())
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_COUNTRY_UPDATE, e);
    }
  }

  @Override
  public int deleteById(Integer id) {
    log.info(logMessages.get(LOG_COUNTRY_DELETING, id));
    try {
      return jdbcClient
          .sql(sqlProperties.deleteById())
          .param(CountrySqlColumns.ID, id)
          .update();
    } catch (DataAccessException e) {
      throw new DatabaseException(ERROR_COUNTRY_DELETE, e);
    }
  }
}
