package com.learning.redis.product;

import java.util.List;

interface ProductService {

  List<ProductResponse> findAll();

  ProductResponse findById(String id);

  ProductResponse create(ProductRequest request);

  ProductResponse update(String id, ProductRequest request);

  void deleteById(String id);

}
