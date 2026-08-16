package com.learning.traefik;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
class OpenApiConfig {

  private static final List<Locale> SUPPORTED_OPENAPI_LOCALES = List.of(Locale.ENGLISH, Locale.forLanguageTag("pt-BR"));

  private static final String OPENAPI_TITLE = "openapi.info.title";
  private static final String OPENAPI_DESCRIPTION = "openapi.info.description";

  private final MessageSource messageSource;

  OpenApiConfig(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(buildInfo(resolveOpenApiLocale()));
  }

  Info buildInfo() {
    return buildInfo(resolveOpenApiLocale());
  }

  Info buildInfo(Locale locale) {
    Locale openApiLocale = resolveSupportedLocale(locale);

    return new Info()
        .title(messageSource.getMessage(OPENAPI_TITLE, null, openApiLocale))
        .version("1.0.0")
        .description(messageSource.getMessage(OPENAPI_DESCRIPTION, null, openApiLocale));
  }

  private Locale resolveOpenApiLocale() {
    return resolveSupportedLocale(LocaleContextHolder.getLocale());
  }

  private Locale resolveSupportedLocale(Locale locale) {
    return SUPPORTED_OPENAPI_LOCALES.stream()
        .filter(candidate -> candidate.getLanguage().equals(locale.getLanguage()))
        .findFirst()
        .orElse(Locale.ROOT);
  }
}
