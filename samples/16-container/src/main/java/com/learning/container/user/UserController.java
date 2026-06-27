package com.learning.container.user;

import org.springframework.web.bind.annotation.RestController;

import com.learning.container.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

  private static final String LOG_HOST_INFO = "log.container.host.info";

  private final LogMessages logMessages;

  UserController(LogMessages logMessages) {
    this.logMessages = logMessages;
  }

  @GetMapping("/hello")
  public String sayHello() throws UnknownHostException {
    String hostName = InetAddress.getLocalHost().getHostName();
    String hostAddress = InetAddress.getLocalHost().getHostAddress();

    log.info(logMessages.get(LOG_HOST_INFO, hostName, hostAddress));

    return String.format("Hostname: %s and IP Address: %s", hostName, hostAddress);
  }
}
