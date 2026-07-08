package com.learning.geography.country;

import org.springframework.http.HttpStatus;

import com.learning.geography.exception.AppException;

class CountryNotFoundException extends AppException {

  CountryNotFoundException(Long id) {
    super("error.country.not.found", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
