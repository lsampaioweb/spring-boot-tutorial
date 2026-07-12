package com.learning.exception_handling.user;

import java.util.List;

interface UserService {

  List<UserResponse> findAll();

  UserResponse findById(Long id);

  UserResponse create(UserRequest request);

  UserResponse update(Long id, UserRequest request);

  boolean delete(Long id);

}
