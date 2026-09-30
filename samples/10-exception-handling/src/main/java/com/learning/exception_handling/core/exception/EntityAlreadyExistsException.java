package com.learning.exception_handling.core.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends AppException {

  public EntityAlreadyExistsException(String prefix, Object[] objects) {
    super(buildKey(prefix), buildErrorCode(prefix, "ALREADY_EXISTS"), objects, HttpStatus.CONFLICT);
  }

  private static String buildKey(String prefix) {
    return String.format("%s.exists", prefix);
  }

  private static String buildErrorCode(String prefix, String suffix) {
    return prefix.toUpperCase(Locale.ROOT) + "_" + suffix;
  }
}
