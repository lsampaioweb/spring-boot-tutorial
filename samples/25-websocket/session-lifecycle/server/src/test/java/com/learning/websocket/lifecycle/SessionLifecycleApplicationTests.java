package com.learning.websocket.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "WEBSOCKET_ADMIN_PASSWORD=session-admin-test-password",
    "app.security.username=session-admin-test",
    "app.security.password=session-admin-test-password",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false",
    "app.websocket.allowed-origins[0]=http://localhost:8093",
    "app.websocket.abuse.max-messages=5",
    "app.websocket.abuse.window-ms=3000"
})
@ActiveProfiles("development")
class SessionLifecycleApplicationTests {

  @Test
  void contextLoads() {
  }
}
