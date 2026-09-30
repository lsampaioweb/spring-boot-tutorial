package com.learning.postgres.user;

/**
 * User domain model (immutable).
 */
record User(Long id, String name, String email) {
}
