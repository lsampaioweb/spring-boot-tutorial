package com.learning.tracing.callee.ping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * REST endpoint that receives propagated W3C trace context from the caller.
 */
@RestController
@RequestMapping("/api/v1/pings")
@RequiredArgsConstructor
class PingRestController {

  private final PingService pingService;

  /**
   * Returns a simple ping payload for the tracing demo.
   *
   * @return ping response
   */
  @GetMapping
  public ResponseEntity<PingResponse> ping() {
    return ResponseEntity.ok(pingService.ping());
  }
}
