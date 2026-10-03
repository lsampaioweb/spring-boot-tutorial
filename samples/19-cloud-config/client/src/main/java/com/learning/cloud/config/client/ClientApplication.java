package com.learning.cloud.config.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.learning.cloud.config.client.hello.HelloConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(HelloConfigurationProperties.class)
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
