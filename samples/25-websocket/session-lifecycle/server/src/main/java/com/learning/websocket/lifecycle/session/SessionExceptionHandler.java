package com.learning.websocket.lifecycle.session;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class SessionExceptionHandler {

  @ExceptionHandler(SessionNotFoundException.class)
  ResponseEntity<Map<String, String>> handleNotFound(SessionNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of(
            "errorCode", "SESSION_NOT_FOUND",
            "sessionId", exception.sessionId()));
  }
}
