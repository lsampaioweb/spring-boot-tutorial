package com.learning.rabbitmq.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages/direct")
@RequiredArgsConstructor
@Tag(name = "{openapi.orders.tag}")
class OrderRestController {

  private final OrderService orderService;

  @PostMapping
  @Operation(summary = "{openapi.orders.submit.summary}")
  public ResponseEntity<OrderResponse> submitOrder(@RequestBody OrderRequest request) {
    return ResponseEntity.accepted().body(orderService.submit(request));
  }
}
