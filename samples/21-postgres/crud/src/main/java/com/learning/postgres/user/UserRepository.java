package com.learning.postgres.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface UserRepository {
  Page<User> findAll(Pageable pageable);

  Optional<User> findById(Long id);

  User insert(User user);

  int update(User user);

  int deleteById(Long id);
}
