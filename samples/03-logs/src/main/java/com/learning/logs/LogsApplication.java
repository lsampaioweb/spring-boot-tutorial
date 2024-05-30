package com.learning.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogsApplication {

  private static final Logger log = LoggerFactory.getLogger(LogsApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(LogsApplication.class, args);

    LogsApplication app = new LogsApplication();
    app.logMessages();
  }

  public void logMessages() {
    log.trace("A TRACE Message");
    log.debug("A DEBUG Message");
    log.info("An INFO Message");
    log.warn("A WARN Message");
    log.error("An ERROR Message");
  }

}
