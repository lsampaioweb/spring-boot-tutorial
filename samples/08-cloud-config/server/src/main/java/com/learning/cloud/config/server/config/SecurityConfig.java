package com.learning.cloud.config.server.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SecurityConfigurationProperties.class)
class SecurityConfig {

  private final SecurityConfigurationProperties securityProperties;

  SecurityConfig(SecurityConfigurationProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

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
            .withUsername(securityProperties.username())
            .password("{noop}" + securityProperties.password())
            .roles("cloud-config-client")
            .build());

    return manager;
  }
}
