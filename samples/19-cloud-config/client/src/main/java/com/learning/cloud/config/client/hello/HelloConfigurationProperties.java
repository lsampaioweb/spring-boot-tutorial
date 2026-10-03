package com.learning.cloud.config.client.hello;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@ConfigurationProperties(prefix = "app.hello")
@Validated
public record HelloConfigurationProperties(@NotBlank String role, @Positive int serverPort) {
}
