package com.learning.geography.country;

import org.springframework.security.access.prepost.PreAuthorize;

import com.learning.geography.common.PagedResponse;

interface CountryService {

  PagedResponse<CountryResponse> findAll(int page, int size);

  CountryResponse findById(Integer id);

  @PreAuthorize("@permissions.canCreate(authentication)")
  CountryResponse create(CreateCountryRequest request);

  @PreAuthorize("@permissions.canUpdate(authentication)")
  CountryResponse update(Integer id, UpdateCountryRequest request);

  @PreAuthorize("@permissions.canDelete(authentication)")
  void delete(Integer id);
}
