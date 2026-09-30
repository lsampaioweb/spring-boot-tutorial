package com.learning.traefik;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
class ActuatorSecurityConfig {

  private final Environment environment;
  private final SecurityProperties securityProperties;

  ActuatorSecurityConfig(Environment environment, SecurityProperties securityProperties) {
    this.environment = environment;
    this.securityProperties = securityProperties;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    String[] publicRoutes = publicRoutes();

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
            .requestMatchers("/actuator/**").authenticated()
            .requestMatchers(publicRoutes).permitAll()
            .anyRequest().denyAll());

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    var actuator = securityProperties.credentials().actuator();

    return new InMemoryUserDetailsManager(
        User.withUsername(actuator.username())
            .password(passwordEncoder.encode(actuator.password()))
            .roles("ACTUATOR")
            .build());
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
