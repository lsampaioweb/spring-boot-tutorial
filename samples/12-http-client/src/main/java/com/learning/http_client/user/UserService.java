package com.learning.http_client.user;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

interface UserService {

  PagedModel<EntityModel<UserResponse>> findAll(Pageable pageable);

  Optional<UserResponse> findById(Integer id);

  UserResponse create(UserRequest request);

  Optional<UserResponse> update(Integer id, UserRequest request);

  boolean delete(Integer id);

}
