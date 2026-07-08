package com.learning.cloud.config.client.hello;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.cloud.config.client.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/hellos")
class HelloController {

  private static final String LOG_SAY_HELLO = "log.say.hello";
  private static final String RESPONSE_MESSAGE = "response.message";

  private final HelloConfigurationProperties properties;
  private final LogMessages logMessages;
  private final MessageSource messageSource;

  public HelloController(HelloConfigurationProperties properties, LogMessages logMessages,
      MessageSource messageSource) {
    this.properties = properties;
    this.logMessages = logMessages;
    this.messageSource = messageSource;
  }

  @GetMapping
  public ResponseEntity<HelloResponse> sayHello() {
    log.info(logMessages.get(LOG_SAY_HELLO));

    String message = messageSource.getMessage(RESPONSE_MESSAGE,
        new Object[] { properties.role(), properties.serverPort() },
        LocaleContextHolder.getLocale());

    return ResponseEntity.ok(new HelloResponse(message));
  }
}
