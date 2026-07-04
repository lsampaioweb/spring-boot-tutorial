package com.learning.postgres.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("PostgreSQL API")
            .version("1.0.0")
            .description("REST API with PostgreSQL persistence"));
  }
}
