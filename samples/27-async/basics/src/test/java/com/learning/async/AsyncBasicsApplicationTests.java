package com.learning.async;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "ASYNC_USER_PASSWORD=async-test-password",
    "app.security.username=async-test-user",
    "app.security.password=async-test-password",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false"
})
@ActiveProfiles("development")
class AsyncBasicsApplicationTests {

  @Test
  void contextLoads() {
  }
}