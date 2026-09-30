package com.learning.postgres.exception;

record ValidationError(String field, String message) {
}
