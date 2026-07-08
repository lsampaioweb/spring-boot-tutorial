package com.learning.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WebSocketServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketServerApplication.class, args);
  }

}
