package com.learning.cloud.config.client.hello;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.learning.cloud.config.client.i18n.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class HelloServiceImpl implements HelloService {

  private static final String LOG_SAY_HELLO = "log.say.hello";
  private static final String RESPONSE_MESSAGE = "response.message";

  private final HelloConfigurationProperties properties;
  private final HelloMapper helloMapper;
  private final LogMessages logMessages;
  private final MessageSource messageSource;

  @Override
  public HelloResponse sayHello() {
    log.info(logMessages.get(LOG_SAY_HELLO));

    Hello hello = helloMapper.toDomain(properties);
    String message = messageSource.getMessage(
        RESPONSE_MESSAGE,
        new Object[] { hello.role(), hello.serverPort() },
        LocaleContextHolder.getLocale());

    return helloMapper.toResponse(message);
  }
}
