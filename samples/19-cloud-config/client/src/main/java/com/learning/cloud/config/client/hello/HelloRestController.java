package com.learning.cloud.config.client.hello;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hellos")
@RequiredArgsConstructor
class HelloRestController {

  private final HelloService helloService;

  @GetMapping
  public ResponseEntity<HelloResponse> sayHello() {
    return ResponseEntity.ok(helloService.sayHello());
  }
}
