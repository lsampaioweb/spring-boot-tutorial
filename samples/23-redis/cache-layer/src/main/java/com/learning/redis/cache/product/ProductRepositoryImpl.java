package com.learning.redis.cache.product;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class ProductRepositoryImpl implements ProductRepository {

  private final JdbcClient jdbcClient;
  private final ProductSqlConfigurationProperties sqlProperties;

  ProductRepositoryImpl(JdbcClient jdbcClient, ProductSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public List<Product> findAll() {
    return jdbcClient.sql(sqlProperties.findAll()).query(Product.class).list();
  }

  @Override
  public Optional<Product> findById(Long id) {
    return jdbcClient.sql(sqlProperties.findById()).param("id", id).query(Product.class).optional();
  }

  @Override
  public Product insert(Product product) {
    return jdbcClient.sql(sqlProperties.insert()).param("name", product.name())
        .param("description", product.description()).param("price", product.price())
        .query(Product.class).single();
  }

  @Override
  public Optional<Product> update(Product product) {
    return jdbcClient.sql(sqlProperties.update()).param("id", product.id()).param("name", product.name())
        .param("description", product.description()).param("price", product.price())
        .query(Product.class).optional();
  }

  @Override
  public boolean deleteById(Long id) {
    return jdbcClient.sql(sqlProperties.deleteById()).param("id", id).update() == 1;
  }
}
