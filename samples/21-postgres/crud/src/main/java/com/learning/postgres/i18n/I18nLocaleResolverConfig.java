package com.learning.postgres.i18n;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for i18n locale resolution via Accept-Language header.
 */
@Configuration
class I18nLocaleResolverConfig implements WebMvcConfigurer {

  /**
   * Register LocaleResolver bean for Accept-Language header resolution.
   */
  @Bean
  LocaleResolver localeResolver() {
    var resolver = new I18nAcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    resolver.setSupportedLocales(List.of(Locale.ENGLISH, Locale.forLanguageTag("pt-BR")));

    return resolver;
  }
}
