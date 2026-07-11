package com.learning.geography.country;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CountryDtoMapper {

  CountryResponse toResponse(Country country);

  @Mapping(target = "id", ignore = true)
  Country toEntity(CreateCountryRequest request);

  @Mapping(target = "id", source = "country.id")
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "isoCode", source = "request.isoCode")
  Country updateEntity(UpdateCountryRequest request, Country country);

}
