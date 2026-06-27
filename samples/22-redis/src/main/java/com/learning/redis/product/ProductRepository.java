package com.learning.redis.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.redis.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class ProductRepository {

  private static final String PRODUCTS_HASH_KEY = "products";
  private static final String LOG_PRODUCT_FETCHING_ALL = "log.product.fetching.all";
  private static final String LOG_PRODUCT_FETCHING_ID = "log.product.fetching.id";
  private static final String LOG_PRODUCT_SAVING = "log.product.saving";
  private static final String LOG_PRODUCT_DELETING_ID = "log.product.deleting.id";
  private static final String ERROR_PRODUCT_SERIALIZE = "error.product.serialize";
  private static final String ERROR_PRODUCT_DESERIALIZE = "error.product.deserialize";

  private final HashOperations<String, String, String> hashOperations;
  private final ObjectMapper objectMapper;
  private final LogMessages logMessages;

  ProductRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
      LogMessages logMessages) {
    this.hashOperations = redisTemplate.opsForHash();
    this.objectMapper = objectMapper;
    this.logMessages = logMessages;
  }

  List<Product> findAll() {
    log.info(logMessages.get(LOG_PRODUCT_FETCHING_ALL));

    return hashOperations.entries(PRODUCTS_HASH_KEY).values().stream()
        .map(this::deserialize)
        .toList();
  }

  Optional<Product> findById(String id) {
    log.info(logMessages.get(LOG_PRODUCT_FETCHING_ID, id));

    String json = hashOperations.get(PRODUCTS_HASH_KEY, id);

    return Optional.ofNullable(json).map(this::deserialize);
  }

  Product save(Product product) {
    log.info(logMessages.get(LOG_PRODUCT_SAVING, product.id()));

    hashOperations.put(PRODUCTS_HASH_KEY, product.id(), serialize(product));

    return product;
  }

  void deleteById(String id) {
    log.info(logMessages.get(LOG_PRODUCT_DELETING_ID, id));

    hashOperations.delete(PRODUCTS_HASH_KEY, id);
  }

  private String serialize(Product product) {
    try {
      return objectMapper.writeValueAsString(product);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(logMessages.get(ERROR_PRODUCT_SERIALIZE, product.id()), e);
    }
  }

  private Product deserialize(String json) {
    try {
      return objectMapper.readValue(json, Product.class);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(logMessages.get(ERROR_PRODUCT_DESERIALIZE), e);
    }
  }

}
