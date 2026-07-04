package com.learning.postgres.user;

/**
 * SQL column name constants for users table.
 * Centralizes column names to avoid hardcoding them throughout repository
 * methods.
 */
class UserSqlColumns {
  private UserSqlColumns() {
    // Utility class.
  }

  static final String ID = "id";
  static final String NAME = "name";
  static final String EMAIL = "email";
}
