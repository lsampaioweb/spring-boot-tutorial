package com.learning.redis.exception;

public record ValidationError(String field, String message) {
}
