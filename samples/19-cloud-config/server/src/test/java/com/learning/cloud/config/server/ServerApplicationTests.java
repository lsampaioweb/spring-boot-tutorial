package com.learning.cloud.config.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "CONFIG_SERVER_PASSWORD=test-server-password",
    "CLOUD_CONFIG_CLIENT_PASSWORD=test-client-password"
})
@ActiveProfiles("development")
class ServerApplicationTests {

  @Test
  void contextLoads() {
  }
}
