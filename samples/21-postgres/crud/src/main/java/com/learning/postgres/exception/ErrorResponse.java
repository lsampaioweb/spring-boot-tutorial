package com.learning.postgres.exception;

import java.time.OffsetDateTime;
import java.util.List;

record ErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String errorCode,
    String message,
    String path,
    String trace,
    List<ValidationError> fields) {
}
