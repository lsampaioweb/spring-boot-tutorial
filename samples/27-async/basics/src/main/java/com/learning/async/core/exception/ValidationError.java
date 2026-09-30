package com.learning.async.core.exception;

/** Identifies one invalid request field and its localized message. */
public record ValidationError(String field, String message) {
}