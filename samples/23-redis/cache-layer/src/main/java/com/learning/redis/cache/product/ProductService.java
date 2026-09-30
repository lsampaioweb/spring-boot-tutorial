package com.learning.redis.cache.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface ProductService {

  Page<ProductResponse> findAll(Pageable pageable);

  ProductResponse findById(Long id);

  ProductResponse create(ProductRequest request);

  ProductResponse update(Long id, ProductRequest request);

  void deleteById(Long id);
}
