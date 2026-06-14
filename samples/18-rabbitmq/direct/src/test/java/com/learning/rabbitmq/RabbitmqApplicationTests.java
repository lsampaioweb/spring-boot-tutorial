package com.learning.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("development")
@TestPropertySource(properties = {
		"spring.rabbitmq.host=localhost",
		"spring.rabbitmq.port=5672",
		"spring.rabbitmq.connection-timeout=1000"
})
class RabbitmqApplicationTests {

	@Test
	void contextLoads() {
		// Test just validates that the context can load.
	}
}
