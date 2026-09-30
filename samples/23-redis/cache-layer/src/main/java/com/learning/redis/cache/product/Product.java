package com.learning.redis.cache.product;

import java.math.BigDecimal;

record Product(Long id, String name, String description, BigDecimal price) {
}