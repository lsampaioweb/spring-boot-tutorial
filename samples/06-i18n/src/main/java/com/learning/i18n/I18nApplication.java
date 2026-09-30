package com.learning.i18n;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class I18nApplication implements CommandLineRunner {

  private static final String GREETING = "greeting";
  private static final String GREETING_MESSAGE = "greeting.message";

  private final MessageSource messageSource;

  public I18nApplication(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public static void main(String[] args) {
    SpringApplication.run(I18nApplication.class, args);
  }

  @Override
  public void run(String... args) {
    Locale english = Locale.ENGLISH;
    Locale portugueseBrazil = Locale.forLanguageTag("pt-BR");

    log.info("English: {}", messageSource.getMessage(GREETING, null, english));
    log.info("English with name: {}", messageSource.getMessage(GREETING_MESSAGE, new Object[] { "Luciano" }, english));
    log.info("Portuguese: {}", messageSource.getMessage(GREETING, null, portugueseBrazil));
    log.info("Portuguese with name: {}",
        messageSource.getMessage(GREETING_MESSAGE, new Object[] { "Luciano" }, portugueseBrazil));
  }

}
