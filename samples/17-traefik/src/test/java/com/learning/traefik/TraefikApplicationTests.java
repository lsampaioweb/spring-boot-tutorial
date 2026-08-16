package com.learning.traefik;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.Resource;

@SpringBootTest
class TraefikApplicationTests {

	@Resource
	private ApplicationContext applicationContext;

	@Test
	void applicationContext_whenBootstrapped_shouldExposeMainApplicationBean() {
		assertThat(applicationContext.getBean(TraefikApplication.class)).isNotNull();
	}

}
