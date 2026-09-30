package com.learning.http_client.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface UserRepository {

  Page<User> findAll(Pageable pageable);

  Optional<User> findById(Long id);

  User create(User user);

  Optional<User> update(Long id, User user);

  boolean delete(Long id);
}
