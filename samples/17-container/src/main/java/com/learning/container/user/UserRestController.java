package com.learning.container.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

  private final HostInfoService hostInfoService;

  @GetMapping("/hello")
  public ResponseEntity<HostInfoResponse> sayHello() {
    return ResponseEntity.ok(hostInfoService.sayHello());
  }
}
