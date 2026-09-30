package com.learning.thymeleaf.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
class TaskServiceImpl implements TaskService {

  private final List<Task> tasks = new ArrayList<>();
  private final AtomicLong counter = new AtomicLong(1);

  @Override
  public List<Task> findAll() {
    return List.copyOf(tasks);
  }

  @Override
  public void add(Task task) {
    task.setId(counter.getAndIncrement());
    tasks.add(task);
  }
}
