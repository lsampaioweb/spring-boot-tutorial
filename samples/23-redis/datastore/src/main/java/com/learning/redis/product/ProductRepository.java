package com.learning.redis.product;

import java.util.List;
import java.util.Optional;

interface ProductRepository {
  List<Product> findAll();

  Optional<Product> findById(String id);

  Product save(Product product);

  void deleteById(String id);
}
