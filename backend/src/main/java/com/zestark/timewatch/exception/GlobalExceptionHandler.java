package com.zestark.timewatch.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for all REST controllers.
 *
 * <p>Centralizes error handling and returns consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles ResourceNotFoundException.
   *
   * @param exception the exception
   * @return 404 Not Found response with error details
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    return buildErrorResponse(
        HttpStatus.NOT_FOUND,
        exception.getMessage(),
        "Resource not found"
    );
  }

  /**
   * Handles DuplicateResourceException.
   *
   * @param exception the exception
   * @return 409 Conflict response with error details
   */
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<Map<String, Object>> handleDuplicateResourceException(
      DuplicateResourceException exception) {
    return buildErrorResponse(
        HttpStatus.CONFLICT,
        exception.getMessage(),
        "Duplicate resource"
    );
  }

  /**
   * Handles TaskSessionConflictException.
   *
   * @param exception the exception
   * @return 409 Conflict response with error details
   */
  @ExceptionHandler(TaskSessionConflictException.class)
  public ResponseEntity<Map<String, Object>> handleTaskSessionConflictException(
      TaskSessionConflictException exception) {
    return buildErrorResponse(
        HttpStatus.CONFLICT,
        exception.getMessage(),
        "Task session conflict"
    );
  }

  /**
   * Handles validation errors (JSR-303 Bean Validation).
   *
   * @param exception the exception
   * @return 400 Bad Request response with validation error details
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      MethodArgumentNotValidException exception) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Validation failed");
    errorResponse.put("errors", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles IllegalArgumentException.
   *
   * @param exception the exception
   * @return 400 Bad Request response with error details
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        "Invalid argument"
    );
  }

  /**
   * Handles all other exceptions.
   *
   * @param exception the exception
   * @return 500 Internal Server Error response with error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception exception) {
    return buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred",
        "Internal server error"
    );
  }

  private ResponseEntity<Map<String, Object>> buildErrorResponse(
      HttpStatus status,
      String message,
      String error) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", status.value());
    errorResponse.put("error", error);
    errorResponse.put("message", message);
    return ResponseEntity.status(status).body(errorResponse);
  }
}
