package com.learning.redis.pubsub;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class ProductAuditListener {

  private final ObjectMapper objectMapper;
  private final AtomicLong receivedEvents = new AtomicLong();

  ProductAuditListener(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void onProductPublished(String payload) {
    try {
      ProductPublishedEvent event = objectMapper.readValue(payload, ProductPublishedEvent.class);
      receivedEvents.incrementAndGet();
      log.info("Received product event for product {}", event.productId());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Could not deserialize product event", e);
    }
  }

  long receivedEventCount() {
    return receivedEvents.get();
  }

}