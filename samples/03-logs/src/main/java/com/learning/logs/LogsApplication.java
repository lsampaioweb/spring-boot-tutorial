package com.learning.logs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.learning.logs.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class LogsApplication implements CommandLineRunner {

  private static final String LOG_TRACE_MESSAGE = "log.trace.message";
  private static final String LOG_DEBUG_MESSAGE = "log.debug.message";
  private static final String LOG_INFO_MESSAGE = "log.info.message";
  private static final String LOG_WARN_MESSAGE = "log.warn.message";
  private static final String LOG_ERROR_MESSAGE = "log.error.message";

  private final LogMessages logMessages;

  public LogsApplication(LogMessages logMessages) {
    this.logMessages = logMessages;
  }

  public static void main(String[] args) {
    SpringApplication.run(LogsApplication.class, args);
  }

  @Override
  public void run(String... args) {
    logMessages();
  }

  private void logMessages() {
    if (log.isTraceEnabled()) {
      log.trace(logMessages.get(LOG_TRACE_MESSAGE));
    }

    if (log.isDebugEnabled()) {
      log.debug(logMessages.get(LOG_DEBUG_MESSAGE));
    }

    if (log.isInfoEnabled()) {
      log.info(logMessages.get(LOG_INFO_MESSAGE));
    }

    if (log.isWarnEnabled()) {
      log.warn(logMessages.get(LOG_WARN_MESSAGE));
    }

    if (log.isErrorEnabled()) {
      log.error(logMessages.get(LOG_ERROR_MESSAGE));
    }
  }

}
