package com.learning.geography.city;

import org.springframework.http.HttpStatus;

import com.learning.geography.exception.AppException;

class CityNotFoundException extends AppException {

  CityNotFoundException(Integer id) {
    super("error.city.not.found", new Object[] { id }, HttpStatus.NOT_FOUND);
  }
}
