package com.learning.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class RabbitMQApplication {

  public static void main(String[] args) {
    SpringApplication.run(RabbitMQApplication.class, args);
  }

}
