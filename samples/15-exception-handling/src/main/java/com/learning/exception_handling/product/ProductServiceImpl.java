package com.learning.exception_handling.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ProductServiceImpl implements ProductService {

  private final ProductDtoMapper mapper;
  private List<Product> products = new ArrayList<>();
  private AtomicLong idCounter = new AtomicLong();

  ProductServiceImpl(ProductDtoMapper mapper) {
    this.mapper = mapper;

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

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponse> findAll() {
    return products.stream().map(mapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponse findById(Long id) {
    return mapper.toResponse(findEntityById(id));
  }

  @Override
  @Transactional
  public ProductResponse create(ProductRequest request) {
    Product entity = mapper.toEntity(request);
    boolean entityExists = products.stream().anyMatch(hasSameIdentity(entity));

    if (entityExists) {
      throw new ProductAlreadyExistsException(entity);
    }

    entity.setId(idCounter.incrementAndGet());
    products.add(entity);

    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {
    Product entity = findEntityById(id);

    entity.setName(request.name());

    return mapper.toResponse(entity);
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    Product entity = findEntityById(id);

    return products.remove(entity);
  }

  private Product findEntityById(Long id) {
    Optional<Product> entity = products.stream().filter(getById(id)).findFirst();

    if (entity.isPresent()) {
      return entity.get();
    }

    throw new ProductNotFoundException(id);
  }

  private Predicate<? super Product> getById(Long id) {
    return u -> u.getId().equals(id);
  }

  private Predicate<? super Product> hasSameIdentity(Product entity) {
    return u -> u.getName().equals(entity.getName());
  }

}
