package com.zestark.timewatch.dto;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Task entity.
 */
public class TaskResponse {

  private UUID id;
  private UUID userId;
  private String taskName;
  private String description;
  private TaskStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public TaskResponse() {
  }

  /**
   * Creates a TaskResponse from a Task entity.
   *
   * @param task the task entity to convert
   * @return a TaskResponse instance
   */
  public static TaskResponse fromEntity(Task task) {
    TaskResponse responseDto = new TaskResponse();
    responseDto.id = task.getId();
    responseDto.userId = task.getUser().getId();
    responseDto.taskName = task.getTaskName();
    responseDto.description = task.getDescription();
    responseDto.status = task.getStatus();
    responseDto.createdAt = task.getCreatedAt();
    responseDto.updatedAt = task.getUpdatedAt();
    return responseDto;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
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

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
