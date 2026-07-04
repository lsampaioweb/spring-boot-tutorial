package com.learning.postgres.user;

/**
 * User domain model (immutable).
 */
public record User(Long id, String name, String email) {
}
