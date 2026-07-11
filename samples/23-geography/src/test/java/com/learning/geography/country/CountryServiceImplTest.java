package com.learning.geography.country;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.learning.geography.i18n.LogMessages;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.learning.geography.common.PagedResponse;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

  @Mock
  CountryRepository countryRepository;

  @Mock
  CountryDtoMapper countryDtoMapper;

  @Mock
  LogMessages logMessages;

  @InjectMocks
  CountryServiceImpl countryService;

  @Test
  void findAll_whenCalled_shouldReturnPagedResponse() {
    Country country1 = new Country(1, "Brazil", "BR");
    Country country2 = new Country(2, "Argentina", "AR");
    CountryResponse response1 = new CountryResponse(1, "Brazil", "BR");
    CountryResponse response2 = new CountryResponse(2, "Argentina", "AR");

    BDDMockito.given(countryRepository.findAll(20, 0)).willReturn(List.of(country1, country2));
    BDDMockito.given(countryRepository.countAll()).willReturn(2L);
    BDDMockito.given(countryDtoMapper.toResponse(country1)).willReturn(response1);
    BDDMockito.given(countryDtoMapper.toResponse(country2)).willReturn(response2);

    PagedResponse<CountryResponse> result = countryService.findAll(0, 20);

    assertThat(result.items()).hasSize(2);
    assertThat(result.items()).containsExactly(response1, response2);
    assertThat(result.totalElements()).isEqualTo(2);
    assertThat(result.page()).isZero();
    assertThat(result.size()).isEqualTo(20);
    assertThat(result.totalPages()).isEqualTo(1);
  }

  @Test
  void findById_whenCountryExists_shouldReturnResponse() {
    Country country = new Country(1, "Brazil", "BR");
    CountryResponse expected = new CountryResponse(1, "Brazil", "BR");

    BDDMockito.given(countryRepository.findById(1)).willReturn(country);
    BDDMockito.given(countryDtoMapper.toResponse(country)).willReturn(expected);

    CountryResponse result = countryService.findById(1);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void findById_whenCountryNotFound_shouldThrowCountryNotFoundException() {
    BDDMockito.given(countryRepository.findById(999)).willReturn(null);

    assertThatThrownBy(() -> countryService.findById(999))
        .isInstanceOf(CountryNotFoundException.class);
  }

  @Test
  void create_whenValidRequest_shouldInsertAndReturnResponse() {
    CreateCountryRequest request = new CreateCountryRequest("Brazil", "BR");
    Country entity = new Country(null, "Brazil", "BR");
    Country created = new Country(1, "Brazil", "BR");
    CountryResponse expected = new CountryResponse(1, "Brazil", "BR");

    BDDMockito.given(countryDtoMapper.toEntity(request)).willReturn(entity);
    BDDMockito.given(countryRepository.insert(entity)).willReturn(created);
    BDDMockito.given(countryDtoMapper.toResponse(created)).willReturn(expected);

    CountryResponse result = countryService.create(request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenCountryExists_shouldUpdateAndReturnResponse() {
    UpdateCountryRequest request = new UpdateCountryRequest("Brazil Updated", "BU");
    Country existing = new Country(1, "Brazil", "BR");
    Country updated = new Country(1, "Brazil Updated", "BU");
    CountryResponse expected = new CountryResponse(1, "Brazil Updated", "BU");

    BDDMockito.given(countryRepository.findById(1)).willReturn(existing);
    BDDMockito.given(countryDtoMapper.updateEntity(request, existing)).willReturn(updated);
    BDDMockito.given(countryDtoMapper.toResponse(updated)).willReturn(expected);

    CountryResponse result = countryService.update(1, request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenCountryNotFound_shouldThrowCountryNotFoundException() {
    BDDMockito.given(countryRepository.findById(999)).willReturn(null);
    UpdateCountryRequest request = new UpdateCountryRequest("Brazil", "BR");

    assertThatThrownBy(() -> countryService.update(999, request))
        .isInstanceOf(CountryNotFoundException.class);
  }

  @Test
  void delete_whenCountryExists_shouldCallDeleteById() {
    BDDMockito.given(countryRepository.deleteById(1)).willReturn(1);

    countryService.delete(1);

    Mockito.verify(countryRepository).deleteById(1);
  }

  @Test
  void delete_whenCountryNotFound_shouldThrowCountryNotFoundException() {
    BDDMockito.given(countryRepository.deleteById(999)).willReturn(0);

    assertThatThrownBy(() -> countryService.delete(999))
        .isInstanceOf(CountryNotFoundException.class);
  }
}
