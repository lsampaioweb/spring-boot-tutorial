package com.learning.redis.product;

import java.util.List;
import java.util.UUID;

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

    return productMapper.toResponse(productRepository.save(product));
  }

  @Override
  @Transactional
  public ProductResponse update(String id, ProductRequest request) {
    findDomainById(id);

    Product updated = new Product(id, request.name(), request.description(), request.price());

    return productMapper.toResponse(productRepository.save(updated));
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    findDomainById(id);

    productRepository.deleteById(id);
  }

  private Product findDomainById(String id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

}
