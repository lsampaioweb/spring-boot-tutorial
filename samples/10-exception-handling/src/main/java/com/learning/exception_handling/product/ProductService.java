package com.learning.exception_handling.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface ProductService {

  Page<ProductResponse> findAll(Pageable pageable);

  ProductResponse findById(Long id);

  ProductResponse create(ProductRequest request);

  ProductResponse update(Long id, ProductRequest request);

  boolean delete(Long id);
}
