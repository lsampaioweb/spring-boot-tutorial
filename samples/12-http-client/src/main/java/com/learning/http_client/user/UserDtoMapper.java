package com.learning.http_client.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface UserDtoMapper {

  UserResponse toResponse(User user);

  @Mapping(target = "id", constant = "0")
  User toEntity(UserRequest request);
}
