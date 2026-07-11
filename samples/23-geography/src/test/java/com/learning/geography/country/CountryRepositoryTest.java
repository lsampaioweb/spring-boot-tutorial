package com.learning.geography.country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import com.learning.geography.i18n.LogMessages;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class CountryRepositoryTest {

  @MockitoBean
  LogMessages logMessages;

  @Autowired
  CountryRepository countryRepository;

  @BeforeEach
  void setUp() {
  }

  @Test
  void findAll_whenTableIsEmpty_shouldReturnEmptyPage() {
    assertThat(countryRepository.findAll(20, 0)).isEmpty();
  }

  @Test
  void countAll_whenTableIsEmpty_shouldReturnZero() {
    assertThat(countryRepository.countAll()).isZero();
  }

  @Test
  void insert_whenNewCountry_shouldGenerateIdAndReturnWithId() {
    Country country = new Country(null, "Brazil", "BR");

    Country result = countryRepository.insert(country);

    assertThat(result.id()).isNotNull();
    assertThat(result.id()).isGreaterThan(0);
    assertThat(result.name()).isEqualTo("Brazil");
    assertThat(result.isoCode()).isEqualTo("BR");
  }

  @Test
  void findById_whenCountryExists_shouldReturnCountry() {
    Country country = countryRepository.insert(new Country(null, "Brazil", "BR"));

    Country result = countryRepository.findById(country.id());

    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("Brazil");
    assertThat(result.isoCode()).isEqualTo("BR");
  }

  @Test
  void findById_whenCountryDoesNotExist_shouldReturnNull() {
    Country result = countryRepository.findById(9999);

    assertThat(result).isNull();
  }

  @Test
  void update_whenCountryExists_shouldReturnRowsAffectedAndPersistChanges() {
    Country inserted = countryRepository.insert(new Country(null, "Brazil", "BR"));
    Country toUpdate = new Country(inserted.id(), "Brasil", "BR");

    int rowsAffected = countryRepository.update(toUpdate);

    assertThat(rowsAffected).isEqualTo(1);
    Country updated = countryRepository.findById(inserted.id());
    assertThat(updated.name()).isEqualTo("Brasil");
  }

  @Test
  void deleteById_whenCountryExists_shouldReturnRowsAffectedAndRemoveCountry() {
    Country inserted = countryRepository.insert(new Country(null, "Brazil", "BR"));

    int rowsAffected = countryRepository.deleteById(inserted.id());

    assertThat(rowsAffected).isEqualTo(1);
    assertThat(countryRepository.findById(inserted.id())).isNull();
  }

  @Test
  void deleteById_whenCountryDoesNotExist_shouldReturnZeroRowsAffected() {
    int rowsAffected = countryRepository.deleteById(999);

    assertThat(rowsAffected).isZero();
  }
}
