package com.learning.app.user;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {

  private static final String APP_VERSION = "1.0";
  // private static final String APP_VERSION = "1.1";
  private static final String BREAK_LINE = "<br />\n";

  @GetMapping("")
  public String sayHello(HttpServletRequest request) {
    StringBuilder response = new StringBuilder();

    response.append("Local IP: ").append(request.getLocalAddr()).append(BREAK_LINE);
    response.append("Remote IP: ").append(request.getRemoteAddr()).append(BREAK_LINE);
    response.append("Thread Id: ").append(Thread.currentThread().getName()).append(BREAK_LINE);
    response.append("App Version: ").append(APP_VERSION).append(BREAK_LINE);

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);

      response.append(headerName).append(": ").append(headerValue).append(BREAK_LINE);
    }

    log.info(response.toString());

    return response.toString();
  }
}
