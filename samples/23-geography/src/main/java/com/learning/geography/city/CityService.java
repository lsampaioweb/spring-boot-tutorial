package com.learning.geography.city;

import java.util.List;

interface CityService {

  List<CityResponse> findAll();

  CityResponse findById(Long id);

  CityResponse create(CreateCityRequest request);

  CityResponse update(Long id, UpdateCityRequest request);

  void delete(Long id);
}
