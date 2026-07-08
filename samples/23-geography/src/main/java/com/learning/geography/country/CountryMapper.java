package com.learning.geography.country;

import org.springframework.stereotype.Component;

@Component
class CountryMapper {

  CountryResponse toResponse(Country country) {
    return new CountryResponse(country.id(), country.name(), country.isoCode());
  }

  Country toEntity(CreateCountryRequest request) {
    return new Country(null, request.name(), request.isoCode());
  }

  Country updateEntity(UpdateCountryRequest request, Country country) {
    return new Country(country.id(), request.name(), request.isoCode());
  }
}
