package com.learning.thymeleaf.config;

import java.util.Locale;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

class I18nAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String acceptLanguage = request.getHeader("Accept-Language");

    if (!StringUtils.hasText(acceptLanguage)) {
      return Locale.ENGLISH;
    }

    return super.resolveLocale(request);
  }
}
