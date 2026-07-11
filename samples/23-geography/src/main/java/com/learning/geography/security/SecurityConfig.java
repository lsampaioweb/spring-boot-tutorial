package com.learning.geography.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security filter chain configuration.
 *
 * Access rules:
 * GET /api/v1/** — public (read-only, no auth required)
 * POST|PUT|DELETE /api/v1/** — requires ROLE_EDITOR
 * /actuator/health/** — public (container liveness and readiness probes)
 * /actuator/** — requires authentication
 * /swagger-ui/** /v3/api-docs/** — public (OpenAPI UI; disabled in production
 * via springdoc config)
 * everything else — denied
 *
 * CSRF is disabled: this is a stateless REST API using HTTP Basic with
 * SessionCreationPolicy.STATELESS; no session cookie is issued, so CSRF does
 * not apply.
 *
 * CORS is not configured here. Add a CorsConfigurationSource bean when a
 * browser-based client from a different origin is introduced.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
            .requestMatchers("/actuator/**").authenticated()
            .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
            // Coarse-grained: require authentication for all write operations.
            // Fine-grained permission (canCreate/canUpdate/canDelete) is enforced by
            // @PreAuthorize.
            .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/v1/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/v1/**").authenticated()
            .anyRequest().denyAll())
        .httpBasic(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

}
