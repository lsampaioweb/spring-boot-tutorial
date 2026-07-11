package com.learning.geography.city;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import com.learning.geography.i18n.LogMessages;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class CityRepositoryTest {

  @MockitoBean
  LogMessages logMessages;

  @Autowired
  CityRepository cityRepository;

  @Autowired
  JdbcClient jdbcClient;

  private Integer stateId;

  @BeforeEach
  void setUp() {
    KeyHolder countryKeyHolder = new GeneratedKeyHolder();
    jdbcClient.sql("INSERT INTO countries (name, iso_code) VALUES (:name, :isoCode)")
        .param("name", "Test Country")
        .param("isoCode", "TC")
        .update(countryKeyHolder);
    Integer countryId = countryKeyHolder.getKey().intValue();

    KeyHolder stateKeyHolder = new GeneratedKeyHolder();
    jdbcClient.sql("INSERT INTO states (country_id, name, abbreviation) VALUES (:countryId, :name, :abbreviation)")
        .param("countryId", countryId)
        .param("name", "Test State")
        .param("abbreviation", "TS")
        .update(stateKeyHolder);
    stateId = stateKeyHolder.getKey().intValue();
  }

  @Test
  void findAll_whenTableIsEmpty_shouldReturnEmptyPage() {
    assertThat(cityRepository.findAll(20, 0)).isEmpty();
  }

  @Test
  void countAll_whenTableIsEmpty_shouldReturnZero() {
    assertThat(cityRepository.countAll()).isZero();
  }

  @Test
  void insert_whenNewCity_shouldGenerateIdAndReturnWithId() {
    City city = new City(null, stateId, "São Paulo");

    City result = cityRepository.insert(city);

    assertThat(result.id()).isNotNull();
    assertThat(result.id()).isGreaterThan(0);
    assertThat(result.stateId()).isEqualTo(stateId);
    assertThat(result.name()).isEqualTo("São Paulo");
  }

  @Test
  void findById_whenCityExists_shouldReturnCity() {
    City inserted = cityRepository.insert(new City(null, stateId, "São Paulo"));

    City result = cityRepository.findById(inserted.id());

    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("São Paulo");
    assertThat(result.stateId()).isEqualTo(stateId);
  }

  @Test
  void findById_whenCityDoesNotExist_shouldReturnNull() {
    City result = cityRepository.findById(9999);

    assertThat(result).isNull();
  }

  @Test
  void update_whenCityExists_shouldReturnRowsAffectedAndPersistChanges() {
    City inserted = cityRepository.insert(new City(null, stateId, "São Paulo"));
    City toUpdate = new City(inserted.id(), stateId, "São Paulo Updated");

    int rowsAffected = cityRepository.update(toUpdate);

    assertThat(rowsAffected).isEqualTo(1);
    City updated = cityRepository.findById(inserted.id());
    assertThat(updated.name()).isEqualTo("São Paulo Updated");
  }

  @Test
  void deleteById_whenCityExists_shouldReturnRowsAffectedAndRemoveCity() {
    City inserted = cityRepository.insert(new City(null, stateId, "São Paulo"));

    int rowsAffected = cityRepository.deleteById(inserted.id());

    assertThat(rowsAffected).isEqualTo(1);
    assertThat(cityRepository.findById(inserted.id())).isNull();
  }

  @Test
  void deleteById_whenCityDoesNotExist_shouldReturnZeroRowsAffected() {
    int rowsAffected = cityRepository.deleteById(999);

    assertThat(rowsAffected).isZero();
  }
}
