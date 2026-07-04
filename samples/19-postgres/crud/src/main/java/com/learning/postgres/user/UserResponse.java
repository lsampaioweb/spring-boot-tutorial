package com.learning.postgres.user;

/**
 * Response DTO for user data.
 */
public record UserResponse(Long id, String name, String email) {
}
