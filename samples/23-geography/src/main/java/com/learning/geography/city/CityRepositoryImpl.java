package com.learning.geography.city;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.learning.geography.exception.DatabaseException;

@Repository
class CityRepositoryImpl implements CityRepository {

  private static final String ERROR_CITY_INSERT = "error.city.insert";
  private static final String ERROR_CITY_UPDATE = "error.city.update";
  private static final String ERROR_CITY_DELETE = "error.city.delete";

  private final JdbcClient jdbcClient;
  private final CitySqlConfigurationProperties sqlProperties;

  CityRepositoryImpl(
      JdbcClient jdbcClient,
      CitySqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<City> findAll(int limit, int offset) {
    return jdbcClient
        .sql(sqlProperties.findAllPaged())
        .param("limit", limit)
        .param("offset", offset)
        .query(City.class)
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
  public City findById(Integer id) {
    return jdbcClient
        .sql(sqlProperties.findById())
        .param(CitySqlColumns.ID, id)
        .query(City.class)
        .optional()
        .orElse(null);
  }

  @Override
  public City insert(City city) {
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
