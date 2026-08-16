package com.learning.restapi.user;

import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface UserService {

  PagedModel<EntityModel<UserResponse>> findAllPaged(int page, int size, String sort);

  Optional<UserResponse> findById(Long id);

  UserResponse create(UserRequest request);

  Optional<UserResponse> update(Long id, UserRequest request);

  boolean delete(Long id);
}
