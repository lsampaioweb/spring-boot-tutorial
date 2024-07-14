package com.learning.exception_handling.product;

import com.learning.exception_handling.core.exception.EntityAlreadyExistsException;

public class ProductAlreadyExistsException extends EntityAlreadyExistsException {

  public ProductAlreadyExistsException(Product entity) {
    super("product", new Object[] { entity.getName() });
  }

}
