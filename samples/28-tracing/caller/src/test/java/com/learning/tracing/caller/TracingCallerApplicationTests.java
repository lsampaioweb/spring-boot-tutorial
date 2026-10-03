package com.learning.tracing.caller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "TRACING_USER_PASSWORD=tracing-test-password",
    "management.opentelemetry.tracing.export.otlp.enabled=false",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false",
    "app.callee.base-url=http://localhost:8081",
    "app.callee.username=tracing-test-user",
    "app.callee.password=tracing-test-password",
    "app.security.username=tracing-test-user",
    "app.security.password=tracing-test-password"
})
@ActiveProfiles("development")
class TracingCallerApplicationTests {

  @Test
  void contextLoads() {
  }
}
