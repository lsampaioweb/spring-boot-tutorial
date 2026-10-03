package com.learning.websocket.lifecycle.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.learning.websocket.lifecycle.client.web.WebSocketClientConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebSocketClientConfigurationProperties.class)
public class SessionLifecycleClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(SessionLifecycleClientApplication.class, args);
  }
}
