package com.learning.exception_handling.product;

import com.learning.exception_handling.core.exception.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {

  public ProductNotFoundException(Long id) {
    super("product", new Object[] { id });
  }

}
