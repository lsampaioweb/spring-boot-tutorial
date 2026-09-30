package com.learning.traefik.user;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class HostInfoServiceImpl implements HostInfoService {

  private static final String HELLO_MESSAGE = "user.hello.message";

  private final MessageSource messageSource;

  @Override
  public HelloResponse sayHello() {
    String message = messageSource.getMessage(HELLO_MESSAGE, null, LocaleContextHolder.getLocale());

    return new HelloResponse(message);
  }
}
