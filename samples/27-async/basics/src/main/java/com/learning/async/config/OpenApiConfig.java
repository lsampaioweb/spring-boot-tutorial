package com.learning.async.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
class OpenApiConfig {

  private final MessageSource messageSource;

  OpenApiConfig(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title(messageSource.getMessage("openapi.info.title", null, Locale.ENGLISH))
            .version("1.0.0")
            .description(messageSource.getMessage("openapi.info.description", null, Locale.ENGLISH)));
  }
}