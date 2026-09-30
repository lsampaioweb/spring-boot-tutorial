package com.learning.redis.pubsub;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

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
    Locale locale = LocaleContextHolder.getLocale();

    return new OpenAPI()
        .info(new Info()
            .title(messageSource.getMessage("openapi.info.title", null, locale))
            .version("1.0.0")
            .description(messageSource.getMessage("openapi.info.description", null, locale)));
  }
}
