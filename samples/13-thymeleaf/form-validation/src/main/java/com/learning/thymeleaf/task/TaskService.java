package com.learning.thymeleaf.task;

import java.util.List;

interface TaskService {

  List<Task> findAll();

  void add(Task task);
}
