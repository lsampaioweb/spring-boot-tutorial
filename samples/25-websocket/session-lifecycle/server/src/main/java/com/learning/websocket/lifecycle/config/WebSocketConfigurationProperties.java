package com.learning.websocket.lifecycle.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app.websocket")
@Validated
public record WebSocketConfigurationProperties(
    @NotEmpty List<String> allowedOrigins,
    @Min(1000) long heartbeatSendIntervalMs,
    @Min(1000) long heartbeatReceiveIntervalMs,
    @NotNull @Valid Abuse abuse) {

  public record Abuse(@Min(1) int maxMessages, @Min(100) long windowMs) {
  }
}
