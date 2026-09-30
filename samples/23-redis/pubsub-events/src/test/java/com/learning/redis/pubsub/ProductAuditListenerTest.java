package com.learning.redis.pubsub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.json.JsonMapper;

class ProductAuditListenerTest {

  @Test
  void countsReceivedProductEvents() throws Exception {
    ProductAuditListener listener = new ProductAuditListener(
        JsonMapper.builder().findAndAddModules().build());
    ProductPublishedEvent event = new ProductPublishedEvent(42L, "Keyboard", Instant.parse("2026-01-01T00:00:00Z"));
    String payload = new String(JsonMapper.builder().findAndAddModules().build().writeValueAsBytes(event));

    listener.onProductPublished(payload);

    assertEquals(1, listener.receivedEventCount());
  }

}