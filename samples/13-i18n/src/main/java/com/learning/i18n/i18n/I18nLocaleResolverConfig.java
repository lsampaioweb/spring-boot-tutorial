package com.learning.i18n.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class I18nLocaleResolverConfig implements WebMvcConfigurer {

  @Bean
  LocaleResolver localeResolver() {
    return new I18nAcceptHeaderLocaleResolver();
  }

}
