package com.learning.geography.state;

import java.util.List;

interface StateService {

  List<StateResponse> findAll();

  StateResponse findById(Long id);

  StateResponse create(CreateStateRequest request);

  StateResponse update(Long id, UpdateStateRequest request);

  void delete(Long id);
}
