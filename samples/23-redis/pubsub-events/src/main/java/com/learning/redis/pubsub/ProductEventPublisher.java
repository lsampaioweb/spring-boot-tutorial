package com.learning.redis.pubsub;

import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
class ProductEventPublisher {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  ProductEventPublisher(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  void publish(ProductEventRequest request) {
    ProductPublishedEvent event = new ProductPublishedEvent(
        request.productId(), request.productName(), Instant.now());
    try {
      redisTemplate.convertAndSend(RedisConfiguration.PRODUCT_EVENTS_CHANNEL,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Could not serialize product event", e);
    }
  }

}