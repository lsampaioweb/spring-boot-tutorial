package com.learning.redis.cache.product;

import org.springframework.stereotype.Component;

@Component
class ProductMapper {

  ProductResponse toResponse(Product product) {
    return new ProductResponse(product.id(), product.name(), product.description(), product.price());
  }
}