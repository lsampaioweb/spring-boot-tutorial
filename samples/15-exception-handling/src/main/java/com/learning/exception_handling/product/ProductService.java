package com.learning.exception_handling.product;

import java.util.List;

interface ProductService {

  List<ProductResponse> findAll();

  ProductResponse findById(Long id);

  ProductResponse create(ProductRequest request);

  ProductResponse update(Long id, ProductRequest request);

  boolean delete(Long id);

}
