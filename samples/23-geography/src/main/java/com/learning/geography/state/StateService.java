package com.learning.geography.state;

import org.springframework.security.access.prepost.PreAuthorize;

import com.learning.geography.common.PagedResponse;

interface StateService {

  PagedResponse<StateResponse> findAll(int page, int size);

  StateResponse findById(Integer id);

  @PreAuthorize("@permissions.canCreate(authentication)")
  StateResponse create(CreateStateRequest request);

  @PreAuthorize("@permissions.canUpdate(authentication)")
  StateResponse update(Integer id, UpdateStateRequest request);

  @PreAuthorize("@permissions.canDelete(authentication)")
  void delete(Integer id);
}
