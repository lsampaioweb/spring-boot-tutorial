package com.learning.geography.city;

import java.util.List;

interface CityRepository {

  List<City> findAll(int limit, int offset);

  long countAll();

  City findById(Integer id);

  City insert(City city);

  int update(City city);

  int deleteById(Integer id);
}
