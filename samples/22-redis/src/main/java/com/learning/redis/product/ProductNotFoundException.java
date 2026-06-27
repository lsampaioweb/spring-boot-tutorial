package com.learning.redis.product;

import org.springframework.http.HttpStatus;

import com.learning.redis.exception.AppException;

public class ProductNotFoundException extends AppException {

  private static final String MESSAGE_KEY = "error.product.not.found";

  public ProductNotFoundException(String id) {
    super(MESSAGE_KEY, HttpStatus.NOT_FOUND, id);
  }

}
