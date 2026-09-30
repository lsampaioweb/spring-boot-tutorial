package com.learning.thymeleaf.task;

import java.util.List;

interface TaskService {

  List<Task> findAll();

  Task add(String title);

  void remove(Long id);
}
