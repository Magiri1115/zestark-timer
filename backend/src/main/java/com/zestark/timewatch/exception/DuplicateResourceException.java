package com.zestark.timewatch.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 *
 * <p>This exception results in a 409 Conflict HTTP response.
 */
public class DuplicateResourceException extends RuntimeException {

  /**
   * Constructor with error message.
   *
   * @param message the error message
   */
  public DuplicateResourceException(String message) {
    super(message);
  }

  /**
   * Constructor with error message and cause.
   *
   * @param message the error message
   * @param cause the cause of this exception
   */
  public DuplicateResourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
