package com.learning.container.user;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class UserController {

  @GetMapping("/hello")
  public String sayHello() throws UnknownHostException {
    String hostName = InetAddress.getLocalHost().getHostName();
    String hostAddress = InetAddress.getLocalHost().getHostAddress();

    String message = String.format("Hostname: %s and IP Address: %s", hostName, hostAddress);

    log.info(message);

    return message;
  }
}
