package com.learning.restapi.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Page<UserResponse> findAll(Pageable pageable);

  Optional<UserResponse> findById(Long id);

  UserResponse create(UserRequest request);

  Optional<UserResponse> update(Long id, UserRequest request);

  boolean delete(Long id);
}
