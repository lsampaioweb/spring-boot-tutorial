package com.learning.http_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class HttpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpClientApplication.class, args);
  }

}
