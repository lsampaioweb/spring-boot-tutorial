package com.learning.redis.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating and updating a product.
 * The {@code id} field is managed by the service — it must not be supplied by
 * the client.
 */
public record ProductRequest(
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin("0.00") BigDecimal price) {
}
