package com.learning.cloud.config.client.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class HelloController {

  private static final String LOG_SAY_HELLO = "log.say.hello";
  private static final String RESPONSE_MESSAGE = "response.message";

  private final String role;
  private final int serverPort;
  private final MessageSource messageSource;

  public HelloController(
      @Value("${user.role}") String role,
      @Value("${server.port}") int serverPort,
      MessageSource messageSource) {
    this.role = role;
    this.serverPort = serverPort;
    this.messageSource = messageSource;
  }

  @GetMapping("/hello")
  public String sayHello() {
    log.info(messageSource.getMessage(LOG_SAY_HELLO, null, LocaleContextHolder.getLocale()));

    return messageSource.getMessage(RESPONSE_MESSAGE, new Object[] { role, serverPort },
        LocaleContextHolder.getLocale());
  }
}
