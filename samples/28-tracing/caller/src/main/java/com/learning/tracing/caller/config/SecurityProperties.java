package com.learning.tracing.caller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.security")
@Validated
record SecurityProperties(@NotBlank String username, @NotBlank String password) {
}
