package com.zestark.timewatch.domain.model;

/**
 * Enum representing the type of task event.
 *
 * <p>This enum defines the possible events that can occur during a task's lifecycle.
 */
public enum TaskEventType {
  /**
   * Task session has started.
   */
  START,

  /**
   * Task session has stopped.
   */
  STOP,

  /**
   * Task session has been paused.
   */
  PAUSE,

  /**
   * Task session has resumed from pause.
   */
  RESUME,

  /**
   * Task has been marked as complete.
   */
  COMPLETE,

  /**
   * Task has been cancelled.
   */
  CANCEL
}
