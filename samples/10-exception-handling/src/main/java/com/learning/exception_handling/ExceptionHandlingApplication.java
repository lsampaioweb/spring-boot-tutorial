package com.learning.exception_handling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class ExceptionHandlingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExceptionHandlingApplication.class, args);
  }

}
