package com.learning.tracing.caller.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Starts a demo trace and calls the callee over RestClient.
 */
@RestController
@RequestMapping("/api/v1/traces")
@RequiredArgsConstructor
class TraceDemoRestController {

  private final TraceDemoService traceDemoService;

  /**
   * Runs the cross-JVM tracing demo.
   *
   * @return combined caller and callee response
   */
  @GetMapping("/demo")
  public ResponseEntity<TraceDemoResponse> demo() {
    return ResponseEntity.ok(traceDemoService.runDemo());
  }
}
