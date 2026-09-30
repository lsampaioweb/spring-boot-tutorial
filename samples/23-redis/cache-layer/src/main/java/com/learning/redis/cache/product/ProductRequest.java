package com.learning.redis.cache.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ProductRequest(
    @NotBlank(message = "{error.validation.name.required}") String name,
    @NotBlank(message = "{error.validation.description.required}") String description,
    @NotNull(message = "{error.validation.price.required}") @DecimalMin(value = "0.01", message = "{error.validation.price.min}") BigDecimal price) {
}
