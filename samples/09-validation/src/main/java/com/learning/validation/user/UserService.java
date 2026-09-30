package com.learning.validation.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Page<UserResponse> findAll(Pageable pageable);

  UserResponse findById(Long id);

  UserResponse create(UserRequest request);

  UserResponse update(Long id, UserRequest request);

  boolean delete(Long id);
}
