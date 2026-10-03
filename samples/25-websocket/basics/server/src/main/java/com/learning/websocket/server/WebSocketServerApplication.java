package com.learning.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/** Starts the WebSocket chat server application. */
@SpringBootApplication
@EnableAsync
public class WebSocketServerApplication {

  /** Launches the application with the supplied command-line arguments. */
  public static void main(String[] args) {
    SpringApplication.run(WebSocketServerApplication.class, args);
  }

}
