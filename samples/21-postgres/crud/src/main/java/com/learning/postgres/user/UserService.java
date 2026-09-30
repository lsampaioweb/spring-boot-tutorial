package com.learning.postgres.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for user business logic.
 */
interface UserService {

  /**
   * Retrieve a page of users.
   */
  Page<UserResponse> findAll(Pageable pageable);

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
