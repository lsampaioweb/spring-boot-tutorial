package com.learning.traefik;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

class OpenApiConfigTest {

  @Test
  void buildInfo_whenLocaleIsEnglish_shouldResolveEnglishMessages() {
    OpenApiConfig openApiConfig = new OpenApiConfig(messageSource());

    var info = openApiConfig.buildInfo(Locale.ENGLISH);

    assertThat(info.getTitle()).isEqualTo("Traefik Integration API");
    assertThat(info.getDescription()).isEqualTo("REST API with Traefik reverse proxy integration.");
    assertThat(info.getVersion()).isEqualTo("1.0.0");
  }

  @Test
  void buildInfo_whenLocaleIsPtBr_shouldResolvePortugueseMessages() {
    OpenApiConfig openApiConfig = new OpenApiConfig(messageSource());

    var info = openApiConfig.buildInfo(Locale.forLanguageTag("pt-BR"));

    assertThat(info.getTitle()).isEqualTo("API de Integracao com Traefik");
    assertThat(info.getDescription()).isEqualTo("API REST com integracao de proxy reverso Traefik.");
    assertThat(info.getVersion()).isEqualTo("1.0.0");
  }

  @Test
  void buildInfo_whenLocaleIsUnsupported_shouldFallbackToDefaultBundleMessages() {
    OpenApiConfig openApiConfig = new OpenApiConfig(messageSource());

    var info = openApiConfig.buildInfo(Locale.forLanguageTag("es-ES"));

    assertThat(info.getTitle()).isEqualTo("Traefik Integration API");
    assertThat(info.getDescription()).isEqualTo("REST API with Traefik reverse proxy integration.");
    assertThat(info.getVersion()).isEqualTo("1.0.0");
  }

  private ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}