package com.learning.exception_handling.core.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String errorCode,
    String message,
    String path,
    String trace,
    List<ValidationError> fields) {
}
