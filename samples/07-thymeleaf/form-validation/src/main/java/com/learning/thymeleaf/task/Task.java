package com.learning.thymeleaf.task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
class Task {

  private Long id;

  @NotBlank(message = "{task.title.required}")
  private String title;

  @Size(max = 200, message = "{task.description.size}")
  private String description;

  @NotBlank(message = "{task.assigned.to.required}")
  @Email(message = "{task.assigned.to.email}")
  private String assignedTo;
}
