package com.learning.exception_handling.user;

import org.springframework.stereotype.Component;

@Component
class UserMapper {

  User toEntity(UserRequest request) {
    return new User(null, request.name(), request.email());
  }

  UserResponse toResponse(User user) {
    return new UserResponse(user.id(), user.name(), user.email());
  }
}
