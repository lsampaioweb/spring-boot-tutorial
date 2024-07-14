package com.learning.exception_handling.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
class ProductService {

  private List<Product> products = new ArrayList<>();
  private AtomicLong idCounter = new AtomicLong();

  public ProductService() {
    products.add(new Product(idCounter.incrementAndGet(), "product-01"));
    products.add(new Product(idCounter.incrementAndGet(), "product-02"));
    products.add(new Product(idCounter.incrementAndGet(), "product-03"));
    products.add(new Product(idCounter.incrementAndGet(), "product-04"));
    products.add(new Product(idCounter.incrementAndGet(), "product-05"));
    products.add(new Product(idCounter.incrementAndGet(), "product-06"));
    products.add(new Product(idCounter.incrementAndGet(), "product-07"));
    products.add(new Product(idCounter.incrementAndGet(), "product-08"));
    products.add(new Product(idCounter.incrementAndGet(), "product-09"));
    products.add(new Product(idCounter.incrementAndGet(), "product-10"));
  }

  List<Product> findAll() {
    return products;
  }

  Product findById(Long id) {
    Optional<Product> entity = products.stream().filter(getById(id)).findFirst();

    if (entity.isPresent()) {
      return entity.get();
    } else {
      throw new ProductNotFoundException(id);
    }
  }

  Product create(Product entity) {
    boolean entityExists = products.stream().anyMatch(equals(entity));

    if (entityExists) {
      throw new ProductAlreadyExistsException(entity);
    } else {
      entity.setId(idCounter.incrementAndGet());

      products.add(entity);

      return entity;
    }
  }

  Product update(Long id, Product entityDetails) {
    Product entity = findById(id);

    entity.setName(entityDetails.getName());

    return entity;
  }

  boolean delete(Long id) {
    Product entity = findById(id);

    return products.remove(entity);
  }

  private Predicate<? super Product> getById(Long id) {
    return u -> u.getId().equals(id);
  }

  private Predicate<? super Product> equals(Product entity) {
    return u -> u.getName().equals(entity.getName());
  }

}
