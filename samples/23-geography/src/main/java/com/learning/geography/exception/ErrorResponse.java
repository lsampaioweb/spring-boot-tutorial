package com.learning.geography.exception;

import java.time.OffsetDateTime;

record ErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String errorCode,
    String message,
    String path,
    String trace) {
}
