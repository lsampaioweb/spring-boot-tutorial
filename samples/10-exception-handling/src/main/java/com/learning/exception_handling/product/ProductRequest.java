package com.learning.exception_handling.product;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank(message = "{error.validation.name.required}") String name) {
}
