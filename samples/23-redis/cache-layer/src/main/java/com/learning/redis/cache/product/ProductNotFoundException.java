package com.learning.redis.cache.product;

import org.springframework.http.HttpStatus;

import com.learning.redis.cache.exception.AppException;

class ProductNotFoundException extends AppException {

  private static final String MESSAGE_KEY = "error.product.not.found";

  ProductNotFoundException(Long id) {
    super(MESSAGE_KEY, HttpStatus.NOT_FOUND, id);
  }
}
