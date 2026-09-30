package com.learning.exception_handling.product;

import org.springframework.stereotype.Component;

@Component
class ProductMapper {

  Product toEntity(ProductRequest request) {
    return new Product(null, request.name());
  }

  ProductResponse toResponse(Product product) {
    return new ProductResponse(product.id(), product.name());
  }
}
