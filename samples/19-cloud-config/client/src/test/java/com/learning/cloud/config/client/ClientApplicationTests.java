package com.learning.cloud.config.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "CLOUD_CONFIG_CLIENT_PASSWORD=unused-in-tests",
    "CLOUD_CONFIG_API_PASSWORD=cloud-config-api-test-password",
    "spring.cloud.config.fail-fast=false",
    "spring.config.import=optional:configserver:",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false",
    "app.hello.role=test",
    "app.hello.server-port=8080",
    "app.security.username=cloud-config-api-test",
    "app.security.password=cloud-config-api-test-password"
})
@ActiveProfiles("development")
class ClientApplicationTests {

  @Test
  void contextLoads() {
  }
}
