package com.learning.websocket.lifecycle.session;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.learning.websocket.lifecycle.config.WebSocketConfigurationProperties;

class MessageRateLimiterTest {

  @Test
  void exceedsLimitAfterConfiguredBurst() {
    WebSocketConfigurationProperties properties = new WebSocketConfigurationProperties(
        java.util.List.of("http://localhost:8093"),
        10_000,
        10_000,
        new WebSocketConfigurationProperties.Abuse(3, 5_000));
    MessageRateLimiter limiter = new MessageRateLimiter(properties);

    assertThat(limiter.exceedsLimit("s1")).isFalse();
    assertThat(limiter.exceedsLimit("s1")).isFalse();
    assertThat(limiter.exceedsLimit("s1")).isFalse();
    assertThat(limiter.exceedsLimit("s1")).isTrue();
  }
}
