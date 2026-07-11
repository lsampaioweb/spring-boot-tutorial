package com.learning.geography.country;

import org.springframework.http.HttpStatus;

import com.learning.geography.exception.AppException;

class CountryNotFoundException extends AppException {

  private static final String ERROR_COUNTRY_NOT_FOUND = "error.country.not.found";

  CountryNotFoundException(Integer id) {
    super(ERROR_COUNTRY_NOT_FOUND, new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
