package com.learning.container.user;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class userController {

  @GetMapping("/hello")
  public String sayHello() {
    String message = "Hello from Docker!!!";
    log.info(message);

    return message;
  }
}
