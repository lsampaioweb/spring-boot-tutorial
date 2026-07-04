package com.learning.postgres.exception;

import java.time.LocalDateTime;

/**
 * Standard error response for all API exceptions.
 */
record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String trace) {
}
