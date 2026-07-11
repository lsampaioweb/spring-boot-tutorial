package com.learning.geography.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Centralized permission policy for @PreAuthorize annotations.
 *
 * Each method expresses a business capability — what the caller wants to do,
 * not who they are. Role-to-permission mapping lives exclusively in this class.
 *
 * Usage:
 * @PreAuthorize("@permissions.canCreate(authentication)")
 * @PreAuthorize("@permissions.canUpdate(authentication)")
 * @PreAuthorize("@permissions.canDelete(authentication)")
 *
 * To change who can perform an operation, modify the method body here only.
 * Service interfaces and SecurityConfig require no changes.
 */
@Component("permissions")
class GeographyPermissions {

  boolean canCreate(Authentication authentication) {
    return hasAnyRole(authentication, Role.EDITOR, Role.ADMIN);
  }

  boolean canUpdate(Authentication authentication) {
    return hasAnyRole(authentication, Role.EDITOR, Role.ADMIN);
  }

  boolean canDelete(Authentication authentication) {
    return hasAnyRole(authentication, Role.EDITOR, Role.ADMIN);
  }

  private boolean hasAnyRole(Authentication authentication, Role... roles) {
    Set<String> userAuthorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());

    return Arrays.stream(roles)
        .anyMatch(role -> userAuthorities.contains(role.authority()));
  }

}
