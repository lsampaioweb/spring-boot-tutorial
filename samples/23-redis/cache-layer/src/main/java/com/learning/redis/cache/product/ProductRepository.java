package com.learning.redis.cache.product;

import java.util.List;
import java.util.Optional;

interface ProductRepository {
  List<Product> findAll();

  Optional<Product> findById(Long id);

  Product insert(Product product);

  Optional<Product> update(Product product);

  boolean deleteById(Long id);
}