package com.learning.cloud.config.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.config.import=optional:configserver:",
	"spring.cloud.config.fail-fast=false"
})
class ClientApplicationTests {

	@Test
	void contextLoads() {
	}

}
