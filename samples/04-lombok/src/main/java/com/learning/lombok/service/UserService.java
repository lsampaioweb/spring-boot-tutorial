package com.learning.lombok.service;

import org.springframework.stereotype.Service;

import com.learning.lombok.LogMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private static final String LOG_USER_DEBUG = "log.user.debug";
  private static final String LOG_USER_INFO = "log.user.info";
  private static final String LOG_USER_ERROR = "log.user.error";

  private final LogMessages logMessages;

  public void logMessage() {
    log.debug(logMessages.get(LOG_USER_DEBUG));
    log.info(logMessages.get(LOG_USER_INFO));
    log.error(logMessages.get(LOG_USER_ERROR));
  }
}
