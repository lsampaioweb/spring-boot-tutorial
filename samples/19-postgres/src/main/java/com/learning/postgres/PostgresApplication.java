package com.learning.postgres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PostgresApplication {

  public static void main(String[] args) {
    SpringApplication.run(PostgresApplication.class, args);
  }

}
