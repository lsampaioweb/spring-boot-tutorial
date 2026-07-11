package com.learning.geography.state;

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
class StateRepositoryTest {

  @MockitoBean
  LogMessages logMessages;

  @Autowired
  StateRepository stateRepository;

  @Autowired
  JdbcClient jdbcClient;

  private Integer countryId;

  @BeforeEach
  void setUp() {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcClient.sql("INSERT INTO countries (name, iso_code) VALUES (:name, :isoCode)")
        .param("name", "Test Country")
        .param("isoCode", "TC")
        .update(keyHolder);
    countryId = keyHolder.getKey().intValue();
  }

  @Test
  void findAll_whenTableIsEmpty_shouldReturnEmptyPage() {
    assertThat(stateRepository.findAll(20, 0)).isEmpty();
  }

  @Test
  void countAll_whenTableIsEmpty_shouldReturnZero() {
    assertThat(stateRepository.countAll()).isZero();
  }

  @Test
  void insert_whenNewState_shouldGenerateIdAndReturnWithId() {
    State state = new State(null, countryId, "São Paulo", "SP");

    State result = stateRepository.insert(state);

    assertThat(result.id()).isNotNull();
    assertThat(result.id()).isGreaterThan(0);
    assertThat(result.countryId()).isEqualTo(countryId);
    assertThat(result.name()).isEqualTo("São Paulo");
    assertThat(result.abbreviation()).isEqualTo("SP");
  }

  @Test
  void findById_whenStateExists_shouldReturnState() {
    State inserted = stateRepository.insert(new State(null, countryId, "São Paulo", "SP"));

    State result = stateRepository.findById(inserted.id());

    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("São Paulo");
    assertThat(result.abbreviation()).isEqualTo("SP");
  }

  @Test
  void findById_whenStateDoesNotExist_shouldReturnNull() {
    State result = stateRepository.findById(9999);

    assertThat(result).isNull();
  }

  @Test
  void update_whenStateExists_shouldReturnRowsAffectedAndPersistChanges() {
    State inserted = stateRepository.insert(new State(null, countryId, "São Paulo", "SP"));
    State toUpdate = new State(inserted.id(), countryId, "São Paulo Updated", "SU");

    int rowsAffected = stateRepository.update(toUpdate);

    assertThat(rowsAffected).isEqualTo(1);
    State updated = stateRepository.findById(inserted.id());
    assertThat(updated.name()).isEqualTo("São Paulo Updated");
    assertThat(updated.abbreviation()).isEqualTo("SU");
  }

  @Test
  void deleteById_whenStateExists_shouldReturnRowsAffectedAndRemoveState() {
    State inserted = stateRepository.insert(new State(null, countryId, "São Paulo", "SP"));

    int rowsAffected = stateRepository.deleteById(inserted.id());

    assertThat(rowsAffected).isEqualTo(1);
    assertThat(stateRepository.findById(inserted.id())).isNull();
  }

  @Test
  void deleteById_whenStateDoesNotExist_shouldReturnZeroRowsAffected() {
    int rowsAffected = stateRepository.deleteById(999);

    assertThat(rowsAffected).isZero();
  }
}
