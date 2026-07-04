package com.learning.redis;

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
        .title("Redis API")
        .version("1.0.0")
        .description("REST API demonstrating Redis as a data store — products are persisted directly in Redis using Hash operations"));
  }

}
