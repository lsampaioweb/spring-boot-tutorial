package com.learning.cloud.config.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  @Order(1)
  SecurityFilterChain client1SecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/cloud-config-client/**")
        .authorizeHttpRequests(
            authorize -> authorize.anyRequest()
                .hasRole("cloud-config-client"))
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    manager.createUser(
        User
            .withUsername("cloud-config-client")
            .password("{noop}password")
            .roles("cloud-config-client")
            .build());

    // manager.createUser(User.withUsername("client2").password("{noop}client2password").roles("CLIENT2").build());

    return manager;
  }
}
