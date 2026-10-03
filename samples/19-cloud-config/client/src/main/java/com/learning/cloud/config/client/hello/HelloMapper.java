package com.learning.cloud.config.client.hello;

import org.springframework.stereotype.Component;

@Component
class HelloMapper {

  Hello toDomain(HelloConfigurationProperties properties) {
    return new Hello(properties.role(), properties.serverPort());
  }

  HelloResponse toResponse(String message) {
    return new HelloResponse(message);
  }
}
