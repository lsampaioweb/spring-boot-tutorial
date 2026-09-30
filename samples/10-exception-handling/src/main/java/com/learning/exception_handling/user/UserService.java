package com.learning.exception_handling.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface UserService {

  Page<UserResponse> findAll(Pageable pageable);

  UserResponse findById(Long id);

  UserResponse create(UserRequest request);

  UserResponse update(Long id, UserRequest request);

  boolean delete(Long id);
}
