package com.learning.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogsApplication {

  Logger logger = LoggerFactory.getLogger(LogsApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(LogsApplication.class, args);

    LogsApplication app = new LogsApplication();
    app.logMessages();
  }

  public void logMessages() {
    logger.trace("A TRACE Message");
    logger.debug("A DEBUG Message");
    logger.info("An INFO Message");
    logger.warn("A WARN Message");
    logger.error("An ERROR Message");
  }

}
