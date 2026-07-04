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
   * Converts create request DTO to User domain object.
   */
  User toEntity(CreateUserRequest request) {
    return new User(null, request.name(), request.email());
  }

  /**
   * Updates User domain object with data from update request DTO.
   */
  User updateEntity(UpdateUserRequest request, User user) {
    return new User(user.id(), request.name(), request.email());
  }
}
