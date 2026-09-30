package com.learning.tracing.callee.ping;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.learning.tracing.callee.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class PingServiceImpl implements PingService {

  private static final String LOG_PING_HANDLED = "log.ping.handled";

  private final LogMessages logMessages;
  private final String applicationName;

  PingServiceImpl(LogMessages logMessages, @Value("${spring.application.name}") String applicationName) {
    this.logMessages = logMessages;
    this.applicationName = applicationName;
  }

  @Override
  public PingResponse ping() {
    log.info(logMessages.get(LOG_PING_HANDLED), applicationName);

    return new PingResponse(applicationName, Instant.now());
  }
}
