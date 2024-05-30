package com.learning.lombok.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

  public void logMessage() {
    log.debug("This is a debug log message");
    log.info("This is an info log message");
    log.error("This is an error log message");
  }
}
