package com.learning.redis.product;

import java.math.BigDecimal;

/**
 * Domain model for a product stored in Redis.
 * Uses a record for immutability; Jackson deserializes via the canonical
 * constructor.
 */
public record Product(String id, String name, String description, BigDecimal price) {
}
