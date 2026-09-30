package com.learning.restapi.user;

import org.springframework.stereotype.Component;

@Component
class UserMapper {

  UserResponse toResponse(User user) {
    return new UserResponse(user.id(), user.name(), user.email());
  }
}