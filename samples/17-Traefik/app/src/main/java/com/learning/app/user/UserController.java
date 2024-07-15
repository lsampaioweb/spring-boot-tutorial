package com.learning.app.user;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {

  @GetMapping("")
  public String sayHello(HttpServletRequest request) {
    String containerId = request.getLocalAddr();
    String threadId = Thread.currentThread().getName();

    String message = String.format("Ip: [%s] - threadId: [%s]", containerId, threadId);
    log.info(message);

    // Retrieve headers sent by Traefik.
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    String xForwardedHost = request.getHeader("X-Forwarded-Host");
    String xForwardedPort = request.getHeader("X-Forwarded-Port");
    String xForwardedProto = request.getHeader("X-Forwarded-Proto");
    String xForwardedServer = request.getHeader("X-Forwarded-Server");
    String xRealIp = request.getHeader("X-Real-Ip");

    // Build the response string.
    StringBuilder responseBuilder = new StringBuilder();
    String BREAK_LINE = "\n";
    responseBuilder.append(message).append(BREAK_LINE)
        .append("X-Forwarded-For: ").append(xForwardedFor).append(BREAK_LINE)
        .append("X-Forwarded-Host: ").append(xForwardedHost).append(BREAK_LINE)
        .append("X-Forwarded-Port: ").append(xForwardedPort).append(BREAK_LINE)
        .append("X-Forwarded-Proto: ").append(xForwardedProto).append(BREAK_LINE)
        .append("X-Forwarded-Server: ").append(xForwardedServer).append(BREAK_LINE)
        .append("X-Real-Ip: ").append(xRealIp).append(BREAK_LINE);

    return responseBuilder.toString();
  }
}
