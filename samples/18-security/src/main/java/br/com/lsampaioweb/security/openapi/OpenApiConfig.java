package br.com.lsampaioweb.security.openapi;

import java.util.Locale;
import java.util.Objects;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class OpenApiConfig {

  private final MessageSource messageSource;

  public OpenApiConfig(MessageSource messageSource) {
    this.messageSource = Objects.requireNonNull(messageSource);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    Locale locale = LocaleContextHolder.getLocale();
    String title = localizedMessage("openapi.info.title", locale);
    String description = localizedMessage("openapi.info.description", locale);
    return new OpenAPI()
        .info(new Info().title(title).version("1.0.0").description(description));
  }

  private String localizedMessage(String key, Locale locale) {
    return messageSource.getMessage(key, null, locale);
  }
}