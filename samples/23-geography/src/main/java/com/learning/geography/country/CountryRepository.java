package com.learning.geography.country;

import java.util.List;

interface CountryRepository {

  List<Country> findAll(int limit, int offset);

  long countAll();

  Country findById(Integer id);

  Country insert(Country country);

  int update(Country country);

  int deleteById(Integer id);
}
