package com.learning.postgres.user;

import org.springframework.stereotype.Component;

/**
 * Maps between User domain object and DTOs.
 */
@Component
class UserMapper {

  /**
   * Converts User domain object to response DTO.
   */
  UserResponse toResponse(User user) {
    return new UserResponse(user.id(), user.name(), user.email());
  }

  /**
   * Converts batch user data to User domain object.
   */
  User toEntity(BatchCreateUserRequest.UserData userData) {
    return new User(null, userData.name(), userData.email());
  }
}
