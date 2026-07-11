package com.learning.geography.security;

/**
 * Application roles — single source of truth for all role names.
 *
 * Adding a new role: add an enum value here.
 * Renaming a role: rename the enum value here only.
 * No changes are required in @PreAuthorize annotations, SecurityConfig, or
 * UserDetailsConfig.
 */
public enum Role {
  EDITOR,
  ADMIN;

  /**
   * Returns the Spring Security authority string (e.g., "ROLE_EDITOR",
   * "ROLE_ADMIN").
   * Used by GeographyPermissions and UserDetailsConfig to avoid hardcoded
   * strings.
   */
  public String authority() {
    return "ROLE_" + name();
  }

}
