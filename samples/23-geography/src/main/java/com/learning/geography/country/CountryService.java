package com.learning.geography.country;

import java.util.List;

interface CountryService {

  List<CountryResponse> findAll();

  CountryResponse findById(Long id);

  CountryResponse create(CreateCountryRequest request);

  CountryResponse update(Long id, UpdateCountryRequest request);

  void delete(Long id);
}
