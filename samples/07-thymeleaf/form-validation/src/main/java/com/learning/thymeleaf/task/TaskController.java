package com.learning.thymeleaf.task;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learning.thymeleaf.i18n.LogMessages;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/tasks")
class TaskController {

  private static final String LOG_TASK_ADDED = "log.task.added";
  private static final String LOG_TASK_LIST = "log.task.list";
  private static final String VIEW = "task/form";
  private static final String REDIRECT = "redirect:/tasks";
  private static final String ATTR_TASK = "task";
  private static final String ATTR_TASKS = "tasks";

  private final TaskService taskService;
  private final LogMessages logMessages;

  TaskController(TaskService taskService, LogMessages logMessages) {
    this.taskService = taskService;
    this.logMessages = logMessages;
  }

  @GetMapping
  String list(Model model) {
    List<Task> tasks = taskService.findAll();
    log.info(logMessages.get(LOG_TASK_LIST, tasks.size()));

    model.addAttribute(ATTR_TASK, new Task());
    model.addAttribute(ATTR_TASKS, tasks);

    return VIEW;
  }

  @PostMapping
  String add(@Valid @ModelAttribute(ATTR_TASK) Task task, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute(ATTR_TASKS, taskService.findAll());
      return VIEW;
    }

    taskService.add(task);
    log.info(logMessages.get(LOG_TASK_ADDED, task.getTitle()));

    return REDIRECT;
  }
}
