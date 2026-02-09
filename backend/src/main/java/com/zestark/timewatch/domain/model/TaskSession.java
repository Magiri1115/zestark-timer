package com.zestark.timewatch.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TaskSession entity representing a time tracking session for a task.
 *
 * <p>This entity maps to the 'task_sessions' table in the database.
 * Each session tracks the start and end time of a task execution.
 * A running session has a null end_time, ensuring only one active session per task
 * through database-level exclusion control (UNIQUE INDEX WHERE end_time IS NULL).
 */
@Entity
@Table(name = "task_sessions")
public class TaskSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /**
   * Default constructor required by JPA.
   */
  public TaskSession() {
    this.createdAt = LocalDateTime.now();
  }

  /**
   * Constructor for creating a new task session with task and start time.
   *
   * @param task the task this session belongs to
   * @param startTime the time when this session started
   */
  public TaskSession(Task task, LocalDateTime startTime) {
    this();
    this.task = task;
    this.startTime = startTime;
  }

  /**
   * Checks if this session is currently running.
   *
   * @return true if the session is running (end_time is null), false otherwise
   */
  public boolean isRunning() {
    return endTime == null;
  }

  /**
   * Gets the duration of this session in seconds.
   *
   * <p>If the session is still running, calculates duration from start_time to now.
   * If the session has ended, calculates duration from start_time to end_time.
   *
   * @return the duration of this session in seconds
   */
  public long getDurationInSeconds() {
    LocalDateTime effectiveEndTime = endTime != null ? endTime : LocalDateTime.now();
    return Duration.between(startTime, effectiveEndTime).getSeconds();
  }

  /**
   * Ends this session at the specified time.
   *
   * @param endTime the time when this session ended
   * @throws IllegalArgumentException if endTime is before startTime
   */
  public void endSession(LocalDateTime endTime) {
    if (endTime.isBefore(startTime)) {
      throw new IllegalArgumentException(
          "End time cannot be before start time. Start: " + startTime + ", End: " + endTime
      );
    }
    this.endTime = endTime;
  }

  public UUID getId() {
    return id;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
