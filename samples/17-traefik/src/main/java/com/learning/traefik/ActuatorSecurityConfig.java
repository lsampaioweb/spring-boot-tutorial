package com.learning.traefik;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class ActuatorSecurityConfig {

  private final Environment environment;

  ActuatorSecurityConfig(Environment environment) {
    this.environment = environment;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    String[] publicRoutes = publicRoutes();

    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
        .requestMatchers("/actuator/**").authenticated()
        .requestMatchers(publicRoutes).permitAll()
      .anyRequest().denyAll())
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  private String[] publicRoutes() {
    List<String> routes = new ArrayList<>();
    boolean apiDocsEnabled = environment.getProperty("springdoc.api-docs.enabled", Boolean.class, Boolean.TRUE);
    boolean swaggerUiEnabled = environment.getProperty("springdoc.swagger-ui.enabled", Boolean.class, Boolean.FALSE);

    routes.add("/api/v1/users/hello");
    routes.add("/error");

    if (apiDocsEnabled) {
      routes.add("/v3/api-docs");
      routes.add("/v3/api-docs/**");
    }

    if (swaggerUiEnabled) {
      routes.add("/swagger-ui.html");
      routes.add("/swagger-ui/**");
    }

    return routes.toArray(String[]::new);
  }
}