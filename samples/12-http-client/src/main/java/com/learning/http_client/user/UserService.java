package com.learning.http_client.user;

import java.util.Optional;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

interface UserService {

  PagedModel<EntityModel<UserResponse>> findAll(int page, int size, String sort);

  Optional<UserResponse> findById(Integer id);

  UserResponse create(UserRequest request);

  Optional<UserResponse> update(Integer id, UserRequest request);

  boolean delete(Integer id);

}
