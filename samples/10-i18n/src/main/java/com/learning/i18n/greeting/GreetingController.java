package com.learning.i18n.greeting;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
public class GreetingController {

  private MessageSource messageSource;

  public GreetingController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("/greet")
  public ResponseEntity<GreetingResponse> greet(@RequestParam(name = "name", defaultValue = "User") String name) {
    String message = messageSource.getMessage("greeting.message", new Object[] { name }, getLocale());

    return ResponseEntity.ok(new GreetingResponse(message));
  }

  private Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }
}
