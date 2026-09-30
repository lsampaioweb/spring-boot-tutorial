package br.com.lsampaioweb.security.i18n;

import java.util.Locale;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityMessages {

  private final MessageSource messageSource;

  public SecurityMessages(MessageSource messageSource) {
    this.messageSource = Objects.requireNonNull(messageSource);
  }

  public String get(String key, Object... arguments) {
    return messageSource.getMessage(key, arguments, LocaleContextHolder.getLocale());
  }

  public String get(Locale locale, String key, Object... arguments) {
    return messageSource.getMessage(key, arguments, locale);
  }
}