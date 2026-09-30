package com.learning.redis.cache.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponse> findAll(Pageable pageable) {
    List<ProductResponse> all = productRepository.findAll().stream().map(productMapper::toResponse).toList();
    List<ProductResponse> sorted = sort(all, pageable.getSort());

    return new PageImpl<>(slice(sorted, pageable), pageable, all.size());
  }

  @Override
  @Cacheable(cacheNames = "products", key = "#id")
  @Transactional(readOnly = true)
  public ProductResponse findById(Long id) {
    return productRepository.findById(id).map(productMapper::toResponse)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  @CacheEvict(cacheNames = "products", allEntries = true)
  @Transactional
  public ProductResponse create(ProductRequest request) {
    Product product = new Product(null, request.name(), request.description(), request.price());
    return productMapper.toResponse(productRepository.insert(product));
  }

  @Override
  @CachePut(cacheNames = "products", key = "#id")
  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {
    Product product = new Product(id, request.name(), request.description(), request.price());
    return productRepository.update(product).map(productMapper::toResponse)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  @CacheEvict(cacheNames = "products", allEntries = true)
  @Transactional
  public void deleteById(Long id) {
    if (!productRepository.deleteById(id)) {
      throw new ProductNotFoundException(id);
    }
  }

  private List<ProductResponse> sort(List<ProductResponse> products, Sort sort) {
    if (sort == null || sort.isUnsorted()) {
      return products;
    }

    List<ProductResponse> sorted = new ArrayList<>(products);

    for (Sort.Order order : sort) {
      Comparator<ProductResponse> comparator = switch (order.getProperty()) {
        case "id" -> (left, right) -> left.id().compareTo(right.id());
        case "name" -> (left, right) -> left.name().compareTo(right.name());
        case "price" -> (left, right) -> left.price().compareTo(right.price());
        default -> throw new IllegalArgumentException("error.sort.property.invalid");
      };

      if (order.isDescending()) {
        comparator = comparator.reversed();
      }

      sorted.sort(comparator);
    }

    return sorted;
  }

  private List<ProductResponse> slice(List<ProductResponse> items, Pageable pageable) {
    int fromIndex = (int) pageable.getOffset();

    if (fromIndex >= items.size()) {
      return List.of();
    }

    int toIndex = Math.min(fromIndex + pageable.getPageSize(), items.size());

    return items.subList(fromIndex, toIndex);
  }

}