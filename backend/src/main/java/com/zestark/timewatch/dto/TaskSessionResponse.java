package com.zestark.timewatch.dto;

import com.zestark.timewatch.domain.model.TaskSession;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for TaskSession entity.
 */
public class TaskSessionResponse {

  private UUID id;
  private UUID taskId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Long durationInSeconds;
  private boolean isRunning;
  private LocalDateTime createdAt;

  public TaskSessionResponse() {
  }

  /**
   * Creates a TaskSessionResponse from a TaskSession entity.
   *
   * @param taskSession the task session entity to convert
   * @return a TaskSessionResponse instance
   */
  public static TaskSessionResponse fromEntity(TaskSession taskSession) {
    TaskSessionResponse responseDto = new TaskSessionResponse();
    responseDto.id = taskSession.getId();
    responseDto.taskId = taskSession.getTask().getId();
    responseDto.startTime = taskSession.getStartTime();
    responseDto.endTime = taskSession.getEndTime();
    responseDto.durationInSeconds = taskSession.getDurationInSeconds();
    responseDto.isRunning = taskSession.isRunning();
    responseDto.createdAt = taskSession.getCreatedAt();
    return responseDto;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getTaskId() {
    return taskId;
  }

  public void setTaskId(UUID taskId) {
    this.taskId = taskId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public Long getDurationInSeconds() {
    return durationInSeconds;
  }

  public void setDurationInSeconds(Long durationInSeconds) {
    this.durationInSeconds = durationInSeconds;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setRunning(boolean running) {
    isRunning = running;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
