package com.learning.geography.i18n;

import java.util.Locale;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

class I18nAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String acceptLanguageHeader = request.getHeader("Accept-Language");

    if (!StringUtils.hasText(acceptLanguageHeader)) {
      return getDefaultLocale();
    }

    // Parse Accept-Language header to extract primary locale
    return Locale.lookup(Locale.LanguageRange.parse(acceptLanguageHeader), getSupportedLocales());
  }
}
