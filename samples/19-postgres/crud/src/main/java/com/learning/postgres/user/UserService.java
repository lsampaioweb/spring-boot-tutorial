package com.learning.postgres.user;

import java.util.List;

/**
 * Service interface for user business logic.
 */
interface UserService {

  /**
   * Retrieve all users.
   */
  List<UserResponse> findAll();

  /**
   * Retrieve a user by ID.
   */
  UserResponse findById(Long id);

  /**
   * Create a new user.
   */
  UserResponse create(CreateUserRequest request);

  /**
   * Update an existing user.
   */
  UserResponse update(Long id, UpdateUserRequest request);

  /**
   * Delete a user by ID.
   */
  int delete(Long id);
}
