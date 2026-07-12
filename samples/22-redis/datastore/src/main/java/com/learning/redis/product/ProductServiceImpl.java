package com.learning.redis.product;

import java.util.List;
import java.util.UUID;

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
  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream()
        .map(productMapper::toResponse)
        .toList();
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

}
