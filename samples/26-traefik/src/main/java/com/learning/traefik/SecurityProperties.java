package com.learning.traefik;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
record SecurityProperties(Credentials credentials) {

  record Credentials(UserCredentials actuator) {
  }

  record UserCredentials(String username, String password) {
  }
}
