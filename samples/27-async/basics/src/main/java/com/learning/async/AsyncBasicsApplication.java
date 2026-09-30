package com.learning.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/** Starts the asynchronous job sample. */
@EnableAsync
@SpringBootApplication
public class AsyncBasicsApplication {

  /** Starts the Spring application context. */
  public static void main(String[] args) {
    SpringApplication.run(AsyncBasicsApplication.class, args);
  }
}