package com.zestark.timewatch.exception;

/**
 * Exception thrown when a task session operation conflicts with existing state.
 *
 * <p>This exception results in a 409 Conflict HTTP response.
 * Common scenarios: attempting to start a session when one is already running,
 * or attempting to stop a session that is not running.
 */
public class TaskSessionConflictException extends RuntimeException {

  /**
   * Constructor with error message.
   *
   * @param message the error message
   */
  public TaskSessionConflictException(String message) {
    super(message);
  }

  /**
   * Constructor with error message and cause.
   *
   * @param message the error message
   * @param cause the cause of this exception
   */
  public TaskSessionConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
