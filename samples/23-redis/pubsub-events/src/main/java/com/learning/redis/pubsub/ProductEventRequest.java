package com.learning.redis.pubsub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

record ProductEventRequest(
    @NotNull @Positive Long productId,
    @NotBlank String productName) {
}