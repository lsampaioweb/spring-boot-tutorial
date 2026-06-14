package com.learning.websocket.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.learning.websocket.client.web.WebSocketClientConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebSocketClientConfigurationProperties.class)
public class WebSocketClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketClientApplication.class, args);
  }

}
