package com.zestark.timewatch.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating a new task.
 */
public class CreateTaskRequest {

  @NotBlank(message = "Task name is required")
  private String taskName;

  private String description;

  public CreateTaskRequest() {
  }

  public CreateTaskRequest(String taskName, String description) {
    this.taskName = taskName;
    this.description = description;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
