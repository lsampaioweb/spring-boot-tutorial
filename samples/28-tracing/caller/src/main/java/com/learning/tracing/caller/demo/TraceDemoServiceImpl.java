package com.learning.tracing.caller.demo;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.learning.tracing.caller.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class TraceDemoServiceImpl implements TraceDemoService {

  private static final String LOG_DEMO_CALLING = "log.demo.calling";
  private static final String LOG_DEMO_CALLEE_RESPONSE = "log.demo.calleeResponse";

  private final RestClient calleeRestClient;
  private final LogMessages logMessages;
  private final String applicationName;

  TraceDemoServiceImpl(
      RestClient calleeRestClient,
      LogMessages logMessages,
      @Value("${spring.application.name}") String applicationName) {
    this.calleeRestClient = calleeRestClient;
    this.logMessages = logMessages;
    this.applicationName = applicationName;
  }

  @Override
  public TraceDemoResponse runDemo() {
    log.info(logMessages.get(LOG_DEMO_CALLING));

    PingResponse ping = calleeRestClient.get()
        .uri("/api/v1/pings")
        .retrieve()
        .body(PingResponse.class);

    String calleeName = ping == null ? "unknown" : ping.service();
    log.info(logMessages.get(LOG_DEMO_CALLEE_RESPONSE), calleeName);

    return new TraceDemoResponse(applicationName, calleeName, Instant.now());
  }
}
