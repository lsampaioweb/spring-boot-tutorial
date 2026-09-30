package com.learning.postgres.i18n;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

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
