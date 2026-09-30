package com.learning.exception_handling.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ProductServiceImpl implements ProductService {

  private final ProductMapper mapper;
  private final List<Product> products = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong();

  ProductServiceImpl(ProductMapper mapper) {
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
  public Page<ProductResponse> findAll(Pageable pageable) {
    List<Product> sorted = getSortedProducts(products, pageable.getSort());
    List<ProductResponse> content = slice(sorted, pageable).stream().map(mapper::toResponse).toList();

    return new PageImpl<>(content, pageable, products.size());
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

    Product created = new Product(idCounter.incrementAndGet(), entity.name());
    products.add(created);

    return mapper.toResponse(created);
  }

  @Override
  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {
    Product entity = findEntityById(id);
    Product updated = new Product(entity.id(), request.name());

    products.remove(entity);
    products.add(updated);

    return mapper.toResponse(updated);
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
    return u -> u.id().equals(id);
  }

  private Predicate<? super Product> hasSameIdentity(Product entity) {
    return u -> u.name().equals(entity.name());
  }

  private List<Product> getSortedProducts(List<Product> products, Sort sort) {
    if (sort == null || sort.isUnsorted()) {
      return products;
    }

    List<Product> sortedProducts = new ArrayList<>(products);

    for (Sort.Order order : sort) {
      Comparator<Product> comparator = switch (order.getProperty()) {
        case "id" -> (left, right) -> left.id().compareTo(right.id());
        case "name" -> (left, right) -> left.name().compareTo(right.name());
        default -> throw new IllegalArgumentException("error.sort.property.invalid");
      };

      if (order.isDescending()) {
        comparator = comparator.reversed();
      }

      sortedProducts.sort(comparator);
    }

    return sortedProducts;
  }

  private List<Product> slice(List<Product> items, Pageable pageable) {
    int fromIndex = (int) pageable.getOffset();

    if (fromIndex >= items.size()) {
      return List.of();
    }

    int toIndex = Math.min(fromIndex + pageable.getPageSize(), items.size());

    return items.subList(fromIndex, toIndex);
  }
}
