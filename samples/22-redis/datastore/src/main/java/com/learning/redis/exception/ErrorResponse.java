package com.learning.redis.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String path,
        String trace) {
}
