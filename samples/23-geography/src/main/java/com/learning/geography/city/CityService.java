package com.learning.geography.city;

import org.springframework.security.access.prepost.PreAuthorize;

import com.learning.geography.common.PagedResponse;

interface CityService {

  PagedResponse<CityResponse> findAll(int page, int size);

  CityResponse findById(Integer id);

  @PreAuthorize("@permissions.canCreate(authentication)")
  CityResponse create(CreateCityRequest request);

  @PreAuthorize("@permissions.canUpdate(authentication)")
  CityResponse update(Integer id, UpdateCityRequest request);

  @PreAuthorize("@permissions.canDelete(authentication)")
  void delete(Integer id);
}
