package com.learning.geography.state;

import java.util.List;

interface StateRepository {

  List<State> findAll(int limit, int offset);

  long countAll();

  State findById(Integer id);

  State insert(State state);

  int update(State state);

  int deleteById(Integer id);
}
