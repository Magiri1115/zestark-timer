package com.zestark.timewatch.exception;

/**
 * Exception thrown when a requested resource is not found.
 *
 * <p>This exception results in a 404 Not Found HTTP response.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructor with error message.
   *
   * @param message the error message
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructor with error message and cause.
   *
   * @param message the error message
   * @param cause the cause of this exception
   */
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
