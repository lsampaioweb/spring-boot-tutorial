package com.learning.redis.cache.product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price) {
}