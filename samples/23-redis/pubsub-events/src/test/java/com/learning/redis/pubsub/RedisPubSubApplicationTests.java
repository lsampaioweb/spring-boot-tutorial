package com.learning.redis.pubsub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "redis.pubsub.listener.enabled=false")
class RedisPubSubApplicationTests {

  @Test
  void contextLoads() {
  }

}