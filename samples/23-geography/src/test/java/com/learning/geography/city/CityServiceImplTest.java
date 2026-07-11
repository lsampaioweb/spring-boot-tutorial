package com.learning.geography.city;

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
class CityServiceImplTest {

  @Mock
  CityRepository cityRepository;

  @Mock
  CityDtoMapper cityDtoMapper;

  @Mock
  LogMessages logMessages;

  @InjectMocks
  CityServiceImpl cityService;

  @Test
  void findAll_whenCalled_shouldReturnPagedResponse() {
    City city1 = new City(1, 20, "São Paulo");
    City city2 = new City(2, 20, "Campinas");
    CityResponse response1 = new CityResponse(1, 20, "São Paulo");
    CityResponse response2 = new CityResponse(2, 20, "Campinas");

    BDDMockito.given(cityRepository.findAll(20, 0)).willReturn(List.of(city1, city2));
    BDDMockito.given(cityRepository.countAll()).willReturn(2L);
    BDDMockito.given(cityDtoMapper.toResponse(city1)).willReturn(response1);
    BDDMockito.given(cityDtoMapper.toResponse(city2)).willReturn(response2);

    PagedResponse<CityResponse> result = cityService.findAll(0, 20);

    assertThat(result.items()).hasSize(2);
    assertThat(result.items()).containsExactly(response1, response2);
    assertThat(result.totalElements()).isEqualTo(2);
    assertThat(result.totalPages()).isEqualTo(1);
  }

  @Test
  void findById_whenCityExists_shouldReturnResponse() {
    City city = new City(1, 20, "São Paulo");
    CityResponse expected = new CityResponse(1, 20, "São Paulo");

    BDDMockito.given(cityRepository.findById(1)).willReturn(city);
    BDDMockito.given(cityDtoMapper.toResponse(city)).willReturn(expected);

    CityResponse result = cityService.findById(1);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void findById_whenCityNotFound_shouldThrowCityNotFoundException() {
    BDDMockito.given(cityRepository.findById(999)).willReturn(null);

    assertThatThrownBy(() -> cityService.findById(999))
        .isInstanceOf(CityNotFoundException.class);
  }

  @Test
  void create_whenValidRequest_shouldInsertAndReturnResponse() {
    CreateCityRequest request = new CreateCityRequest(20, "São Paulo");
    City entity = new City(null, 20, "São Paulo");
    City created = new City(1, 20, "São Paulo");
    CityResponse expected = new CityResponse(1, 20, "São Paulo");

    BDDMockito.given(cityDtoMapper.toEntity(request)).willReturn(entity);
    BDDMockito.given(cityRepository.insert(entity)).willReturn(created);
    BDDMockito.given(cityDtoMapper.toResponse(created)).willReturn(expected);

    CityResponse result = cityService.create(request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenCityExists_shouldUpdateAndReturnResponse() {
    UpdateCityRequest request = new UpdateCityRequest(20, "São Paulo Updated");
    City existing = new City(1, 20, "São Paulo");
    City updated = new City(1, 20, "São Paulo Updated");
    CityResponse expected = new CityResponse(1, 20, "São Paulo Updated");

    BDDMockito.given(cityRepository.findById(1)).willReturn(existing);
    BDDMockito.given(cityDtoMapper.updateEntity(request, existing)).willReturn(updated);
    BDDMockito.given(cityDtoMapper.toResponse(updated)).willReturn(expected);

    CityResponse result = cityService.update(1, request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenCityNotFound_shouldThrowCityNotFoundException() {
    BDDMockito.given(cityRepository.findById(999)).willReturn(null);
    UpdateCityRequest request = new UpdateCityRequest(20, "São Paulo");

    assertThatThrownBy(() -> cityService.update(999, request))
        .isInstanceOf(CityNotFoundException.class);
  }

  @Test
  void delete_whenCityExists_shouldCallDeleteById() {
    BDDMockito.given(cityRepository.deleteById(1)).willReturn(1);

    cityService.delete(1);

    Mockito.verify(cityRepository).deleteById(1);
  }

  @Test
  void delete_whenCityNotFound_shouldThrowCityNotFoundException() {
    BDDMockito.given(cityRepository.deleteById(999)).willReturn(0);

    assertThatThrownBy(() -> cityService.delete(999))
        .isInstanceOf(CityNotFoundException.class);
  }
}
