package com.learning.postgres.user;

import java.util.List;

interface UserRepository {
  List<User> findAll();

  int[] batchInsert(List<User> users);
}
