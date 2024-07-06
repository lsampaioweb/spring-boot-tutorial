package com.learning.i18n.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class I18nAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

  private static final String SESSION_LOCALE_ATTRIBUTE = "locale";
  private static final Locale LOCALE_PT_BR = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String languageParam = request.getParameter("lang");
    if (languageParam != null && !languageParam.isEmpty()) {

      Locale locale = Locale.forLanguageTag(languageParam);
      setLocaleInSession(request, locale);

      return locale;
    }

    // Check if the user's preferred locale is already in session.
    Locale localeInSession = (Locale) request.getSession().getAttribute(SESSION_LOCALE_ATTRIBUTE);
    if (localeInSession != null) {
      return localeInSession;
    }

    String acceptLanguage = request.getHeader("Accept-Language");
    if (acceptLanguage == null || acceptLanguage.isEmpty()) {

      Locale locale = Locale.getDefault();
      setLocaleInSession(request, locale);

      return locale;
    }

    // Parse the Accept-Language header and handle quality values (q=...)
    List<Locale.LanguageRange> acceptLocales = Locale.LanguageRange.parse(acceptLanguage);

    // Match the best locale from the available locales
    Locale bestMatch = Locale.lookup(acceptLocales, getAvailableLocales());

    Locale locale = (bestMatch != null) ? bestMatch : Locale.getDefault();
    setLocaleInSession(request, locale);

    return locale;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    setLocaleInSession(request, locale);

    super.setLocale(request, response, locale);
  }

  private void setLocaleInSession(HttpServletRequest request, Locale locale) {
    request.getSession().setAttribute(SESSION_LOCALE_ATTRIBUTE, locale);
  }

  private List<Locale> getAvailableLocales() {
    List<Locale> availableLocales = new ArrayList<>();

    availableLocales.add(Locale.ENGLISH);
    availableLocales.add(LOCALE_PT_BR);

    return availableLocales;
  }
}
