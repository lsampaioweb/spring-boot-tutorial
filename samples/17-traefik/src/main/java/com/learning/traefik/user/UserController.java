package com.learning.traefik.user;

import java.net.InetAddress;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(UserController.API_V1_BASE_PATH)
@RequiredArgsConstructor
public class UserController {

  public static final String API_V1_BASE_PATH = "/api/v1";
  private static final String HELLO_PATH = "/hello";
  private static final String HOST_INFO = "host.info";
  private static final String HOST_INFO_ERROR = "error.host.info";

  private final MessageSource messageSource;

  @GetMapping(HELLO_PATH)
  public ResponseEntity<String> sayHello() {
    try {
      InetAddress localHost = InetAddress.getLocalHost();

      String hostName = localHost.getHostName();
      String hostAddress = localHost.getHostAddress();

      String message = getMessage(HOST_INFO, hostName, hostAddress);

      log.info(message);
      return ResponseEntity.ok(message);

    } catch (Exception e) {
      String message = getMessage(HOST_INFO_ERROR);
      log.error(message, e);

      return ResponseEntity
          .status(500)
          .body(message);
    }
  }

  private String getMessage(String messageKey, Object... args) {
    return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
  }
}