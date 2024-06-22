package com.learning.cloud.config.client.hello;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class helloController {

  @Value("${user.role}")
  private String role;

  @Value("${server.port}")
  private int serverPort;

  @GetMapping("/hello")
  public String sayHello() {
    log.info("Method: sayHello");

    String message = String.format("Message: %s - %d", role, serverPort);

    log.info(message);

    return message;
  }
}
