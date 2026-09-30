package com.learning.exception_handling.core.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AppException {

  public EntityNotFoundException(String prefix, Object[] objects) {
    super(buildKey(prefix), buildErrorCode(prefix, "NOT_FOUND"), objects, HttpStatus.NOT_FOUND);
  }

  private static String buildKey(String prefix) {
    return String.format("%s.notfound", prefix);
  }

  private static String buildErrorCode(String prefix, String suffix) {
    return prefix.toUpperCase(Locale.ROOT) + "_" + suffix;
  }
}
