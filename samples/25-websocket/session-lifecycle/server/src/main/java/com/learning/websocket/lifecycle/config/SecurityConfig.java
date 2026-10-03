package com.learning.websocket.lifecycle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      SecurityProperties securityProperties,
      @Value("${springdoc.swagger-ui.enabled:false}") boolean swaggerUiEnabled) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> {
          authorize.requestMatchers("/actuator/health", "/actuator/health/**").permitAll();
          authorize.requestMatchers("/actuator/**").authenticated();
          // SockJS/STOMP handshake and transports stay open for this lifecycle demo.
          authorize.requestMatchers("/ws", "/ws/**").permitAll();
          if (swaggerUiEnabled) {
            authorize.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
          }
          authorize.requestMatchers("/api/v1/sessions/**").hasRole("SESSION_ADMIN");
          authorize.anyRequest().denyAll();
        });

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService userDetailsService(SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
    return new InMemoryUserDetailsManager(
        User.withUsername(securityProperties.username())
            .password(passwordEncoder.encode(securityProperties.password()))
            .roles("SESSION_ADMIN")
            .build());
  }
}
