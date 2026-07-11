package com.learning.geography.city;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CityDtoMapper {

  CityResponse toResponse(City city);

  @Mapping(target = "id", ignore = true)
  City toEntity(CreateCityRequest request);

  @Mapping(target = "id", source = "city.id")
  @Mapping(target = "stateId", source = "request.stateId")
  @Mapping(target = "name", source = "request.name")
  City updateEntity(UpdateCityRequest request, City city);

}
