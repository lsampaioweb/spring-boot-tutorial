package com.learning.mapstruct.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface UserMapper {

  UserResponse toResponse(User user);

  @Mapping(target = "id", constant = "0L")
  User toNewEntity(UserRequest request);

  @Mapping(target = "id", source = "id")
  User toEntity(Long id, UserRequest request);
}
