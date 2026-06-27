package com.learning.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisApplicationTests {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void contextLoads_whenApplicationStarts_shouldLoadContext() {
    assertThat(applicationContext).isNotNull();
  }

}
