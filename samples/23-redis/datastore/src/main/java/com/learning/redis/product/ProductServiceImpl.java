package com.learning.redis.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.redis.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class ProductServiceImpl implements ProductService {

  private static final String LOG_PRODUCT_SAVING = "log.product.saving";
  private static final String LOG_PRODUCT_DELETING_ID = "log.product.deleting.id";

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final LogMessages logMessages;

  ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, LogMessages logMessages) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponse> findAll(Pageable pageable) {
    List<ProductResponse> all = productRepository.findAll().stream()
        .map(productMapper::toResponse)
        .toList();
    List<ProductResponse> sorted = sort(all, pageable.getSort());

    return new PageImpl<>(slice(sorted, pageable), pageable, all.size());
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponse findById(String id) {
    return productRepository.findById(id)
        .map(productMapper::toResponse)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  @Transactional
  public ProductResponse create(ProductRequest request) {
    Product product = new Product(
        UUID.randomUUID().toString(),
        request.name(),
        request.description(),
        request.price());

    log.info(logMessages.get(LOG_PRODUCT_SAVING, product.id()));

    return productMapper.toResponse(productRepository.save(product));
  }

  @Override
  @Transactional
  public ProductResponse update(String id, ProductRequest request) {
    findDomainById(id);

    Product updated = new Product(id, request.name(), request.description(), request.price());

    log.info(logMessages.get(LOG_PRODUCT_SAVING, id));

    return productMapper.toResponse(productRepository.save(updated));
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    findDomainById(id);

    log.info(logMessages.get(LOG_PRODUCT_DELETING_ID, id));

    productRepository.deleteById(id);
  }

  private Product findDomainById(String id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));
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
