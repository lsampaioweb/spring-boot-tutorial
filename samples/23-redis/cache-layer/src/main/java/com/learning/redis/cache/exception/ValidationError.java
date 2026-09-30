package com.learning.redis.cache.exception;

public record ValidationError(String field, String message) {
}
