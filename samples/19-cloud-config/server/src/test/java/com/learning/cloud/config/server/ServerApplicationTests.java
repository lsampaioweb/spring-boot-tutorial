package com.learning.cloud.config.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
  "USERNAME=test-user",
  "PASSWORD=test-password",
  "CLOUD_CONFIG_CLIENT_PASSWORD=test-client-password",
  "KEY_STORE_PASSWORD_MY_HTTPS_APP=test-password",
  "server.ssl.enabled=false"
})
class ServerApplicationTests {

  @Test
  void contextLoads() {
  }

}
