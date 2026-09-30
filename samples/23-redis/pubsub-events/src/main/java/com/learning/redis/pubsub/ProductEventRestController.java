package com.learning.redis.pubsub;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/product-events")
@Tag(name = "{openapi.product-events.tag}")
class ProductEventRestController {

  private final ProductEventPublisher eventPublisher;
  private final ProductAuditListener auditListener;

  ProductEventRestController(ProductEventPublisher eventPublisher, ProductAuditListener auditListener) {
    this.eventPublisher = eventPublisher;
    this.auditListener = auditListener;
  }

  @PostMapping
  @Operation(summary = "{openapi.product-events.publish.summary}")
  ResponseEntity<Void> publish(@Valid @RequestBody ProductEventRequest request) {
    eventPublisher.publish(request);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/received")
  @Operation(summary = "{openapi.product-events.received.summary}")
  ResponseEntity<Map<String, Long>> received() {
    return ResponseEntity.ok(Map.of("receivedEvents", auditListener.receivedEventCount()));
  }

}
