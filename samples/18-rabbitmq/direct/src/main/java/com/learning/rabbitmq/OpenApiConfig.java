package com.learning.rabbitmq;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("RabbitMQ Direct Exchange API")
        .version("1.0.0")
        .description("REST API with RabbitMQ direct exchange messaging"));
  }
}
