package com.learning.redis.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface ProductService {

  Page<ProductResponse> findAll(Pageable pageable);

  ProductResponse findById(String id);

  ProductResponse create(ProductRequest request);

  ProductResponse update(String id, ProductRequest request);

  void deleteById(String id);
}
