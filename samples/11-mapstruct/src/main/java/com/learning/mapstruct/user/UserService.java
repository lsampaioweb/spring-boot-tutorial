package com.learning.mapstruct.user;

import java.util.List;

public interface UserService {

  List<UserResponse> findAll();

  UserResponse findById(Long id);

  UserResponse create(UserRequest request);

  UserResponse update(Long id, UserRequest request);

  boolean delete(Long id);
}
