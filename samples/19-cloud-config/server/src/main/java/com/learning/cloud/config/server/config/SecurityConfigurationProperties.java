package com.learning.cloud.config.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.security")
@Validated
record SecurityConfigurationProperties(
    @Valid Principal server,
    @Valid Principal cloudConfigClient) {

  record Principal(@NotBlank String username, @NotBlank String password) {
  }
}
