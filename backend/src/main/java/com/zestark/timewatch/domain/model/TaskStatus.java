package com.zestark.timewatch.domain.model;

/**
 * Enum representing the status of a task.
 *
 * <p>This enum defines the possible states a task can be in during its lifecycle.
 */
public enum TaskStatus {
  /**
   * Task is created but not yet started.
   */
  PENDING,

  /**
   * Task is currently running with an active timer session.
   */
  RUNNING,

  /**
   * Task has been completed successfully.
   */
  COMPLETED,

  /**
   * Task has been cancelled and will not be completed.
   */
  CANCELLED
}
