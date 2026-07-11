package com.learning.geography.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableConfigurationProperties(SecurityConfigurationProperties.class)
public class PermissionsTestConfig {

  @Bean
  GeographyPermissions geographyPermissions() {
    return new GeographyPermissions();
  }
}
