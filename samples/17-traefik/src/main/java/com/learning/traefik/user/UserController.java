package com.learning.traefik.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(UserController.API_V1_BASE_PATH)
@RequiredArgsConstructor
public class UserController {

  public static final String API_V1_BASE_PATH = "/api/v1/users";
  private static final String HELLO_PATH = "/hello";

  private final HostInfoService hostInfoService;

  @GetMapping(HELLO_PATH)
  public ResponseEntity<HelloResponse> sayHello() throws java.net.UnknownHostException {
    return ResponseEntity.ok(hostInfoService.getHostInfo());
  }
}