package com.learning.async.core.exception;

import java.time.OffsetDateTime;
import java.util.List;

/** Provides the standard localized HTTP error representation. */
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