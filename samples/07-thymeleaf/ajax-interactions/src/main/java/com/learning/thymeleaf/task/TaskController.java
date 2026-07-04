package com.learning.thymeleaf.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.learning.thymeleaf.i18n.LogMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/tasks")
class TaskController {

  private static final String LOG_TASK_ADDED = "log.task.added";
  private static final String LOG_TASK_REMOVED = "log.task.removed";
  private static final String LOG_TASK_LIST = "log.task.list";

  private final TaskService taskService;
  private final LogMessages logMessages;

  TaskController(TaskService taskService, LogMessages logMessages) {
    this.taskService = taskService;
    this.logMessages = logMessages;
  }

  // Full page — initial load.
  @GetMapping
  String list(Model model) {
    var tasks = taskService.findAll();
    log.info(logMessages.get(LOG_TASK_LIST, tasks.size()));

    model.addAttribute("tasks", tasks);
    return "task/index";
  }

  // AJAX add — Thymeleaf resolves the fragment expression and returns only the
  // new row HTML; the view resolver renders "task/index :: task-row" for us.
  @PostMapping
  String add(@RequestParam String title, Model model) {
    Task task = taskService.add(title);
    log.info(logMessages.get(LOG_TASK_ADDED, task.title()));

    model.addAttribute("task", task);
    return "task/index :: task-row";
  }

  // AJAX delete — 204 No Content; JS removes the row from the DOM client-side.
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void remove(@PathVariable Long id) {
    taskService.remove(id);
    log.info(logMessages.get(LOG_TASK_REMOVED, id));
  }
}
