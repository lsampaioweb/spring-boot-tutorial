package com.learning.exception_handling.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface UserDtoMapper {

  @Mapping(target = "id", ignore = true)
  User toEntity(UserRequest request);

  UserResponse toResponse(User user);
}
