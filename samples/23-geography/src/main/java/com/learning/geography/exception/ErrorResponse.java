package com.learning.geography.exception;

import java.time.LocalDateTime;

record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String trace) {
}
