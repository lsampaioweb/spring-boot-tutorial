package com.learning.geography.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * User details configuration — in-memory store.
 *
 * To switch to a database-backed or external identity provider:
 * 1. Remove or annotate this class with @Profile("development").
 * 2. Provide a new @Bean of type UserDetailsService backed by
 * JdbcUserDetailsManager,
 * a custom repository-backed implementation, LDAP, or an OAuth2/OIDC resource
 * server.
 * 3. SecurityConfig requires no changes — it depends on the UserDetailsService
 * interface,
 * not this implementation.
 *
 * Credentials are injected from environment variables APP_SECURITY_USERNAME
 * and APP_SECURITY_PASSWORD via SecurityConfigurationProperties.
 */
@Configuration
class UserDetailsConfig {

  @Bean
  UserDetailsService userDetailsService(SecurityConfigurationProperties properties) {
    UserDetails editor = User.withUsername(properties.username())
        .password(passwordEncoder().encode(properties.password()))
        .roles(Role.EDITOR.name())
        .build();

    return new InMemoryUserDetailsManager(editor);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
