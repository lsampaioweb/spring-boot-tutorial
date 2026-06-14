package com.learning.websocket.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSocketClientApplicationTests {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void contextLoads_whenApplicationStarts_shouldLoadContext() {
    assertThat(applicationContext).isNotNull();

    // Intentionally empty: verifies Spring context starts with MVC and Thymeleaf
    // configuration.
  }

}
