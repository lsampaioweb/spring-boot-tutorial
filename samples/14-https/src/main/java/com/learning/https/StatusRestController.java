package com.learning.https;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/https")
class StatusRestController {

  private final MessageSource messageSource;

  StatusRestController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("/status")
  ResponseEntity<StatusResponse> status() {
    Locale locale = LocaleContextHolder.getLocale();

    return ResponseEntity.ok(new StatusResponse(messageSource.getMessage("https.status", null, locale)));
  }

  record StatusResponse(String message) {
  }
}
