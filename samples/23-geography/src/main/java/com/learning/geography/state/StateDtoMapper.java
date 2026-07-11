package com.learning.geography.state;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
interface StateDtoMapper {

  StateResponse toResponse(State state);

  @Mapping(target = "id", ignore = true)
  State toEntity(CreateStateRequest request);

  @Mapping(target = "id", source = "state.id")
  @Mapping(target = "countryId", source = "request.countryId")
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "abbreviation", source = "request.abbreviation")
  State updateEntity(UpdateStateRequest request, State state);

}
