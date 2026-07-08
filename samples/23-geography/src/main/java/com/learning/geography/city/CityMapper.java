package com.learning.geography.city;

import org.springframework.stereotype.Component;

@Component
class CityMapper {

  CityResponse toResponse(City city) {
    return new CityResponse(city.id(), city.stateId(), city.name());
  }

  City toEntity(CreateCityRequest request) {
    return new City(null, request.stateId(), request.name());
  }

  City updateEntity(UpdateCityRequest request, City city) {
    return new City(city.id(), request.stateId(), request.name());
  }
}
