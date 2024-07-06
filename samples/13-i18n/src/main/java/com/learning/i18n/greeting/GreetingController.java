package com.learning.i18n.greeting;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private MessageSource messageSource;

  public GreetingController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("/greet")
  public String greet(@RequestParam(name = "name", defaultValue = "User") String name) {

    String greeting = messageSource.getMessage("greeting.message", new Object[] { name }, getLocale());

    return greeting;
  }

  private Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }
}
