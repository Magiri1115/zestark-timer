package com.zestark.timewatch.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zestark.timewatch.domain.model.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for TaskSession domain model.
 */
@DisplayName("TaskSessionドメインモデルテスト")
class TaskSessionTest {

  private Task testTask;
  private TaskSession taskSession;

  /**
   * Setup test data before each test.
   */
  @BeforeEach
  void setUp() {
    User testUser = new User();
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");

    testTask = new Task();
    testTask.setUser(testUser);
    testTask.setTaskName("Test Task");
    testTask.setStatus(TaskStatus.PENDING);

    taskSession = new TaskSession();
    taskSession.setTask(testTask);
    taskSession.setStartTime(LocalDateTime.now().minusMinutes(10));
  }

  @Test
  @DisplayName("isRunning_shouldReturnTrue_whenEndTimeIsNull")
  void isRunning_shouldReturnTrue_whenEndTimeIsNull() {
    // Arrange
    taskSession.setEndTime(null);

    // Act
    boolean actualIsRunning = taskSession.isRunning();

    // Assert
    assertTrue(actualIsRunning);
  }

  @Test
  @DisplayName("isRunning_shouldReturnFalse_whenEndTimeIsNotNull")
  void isRunning_shouldReturnFalse_whenEndTimeIsNotNull() {
    // Arrange
    taskSession.setEndTime(LocalDateTime.now());

    // Act
    boolean actualIsRunning = taskSession.isRunning();

    // Assert
    assertFalse(actualIsRunning);
  }

  @Test
  @DisplayName("getDurationInSeconds_shouldCalculateCorrectly_whenSessionIsRunning")
  void getDurationInSeconds_shouldCalculateCorrectly_whenSessionIsRunning() {
    // Arrange
    LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
    taskSession.setStartTime(startTime);
    taskSession.setEndTime(null);

    // Act
    long actualDurationInSeconds = taskSession.getDurationInSeconds();

    // Assert
    // Should be approximately 300 seconds (5 minutes), allow 2 seconds tolerance
    assertTrue(actualDurationInSeconds >= 298 && actualDurationInSeconds <= 302,
        "Duration should be approximately 300 seconds, but was: " + actualDurationInSeconds);
  }

  @Test
  @DisplayName("getDurationInSeconds_shouldCalculateCorrectly_whenSessionIsCompleted")
  void getDurationInSeconds_shouldCalculateCorrectly_whenSessionIsCompleted() {
    // Arrange
    LocalDateTime startTime = LocalDateTime.now().minusMinutes(10);
    LocalDateTime endTime = LocalDateTime.now().minusMinutes(5);
    taskSession.setStartTime(startTime);
    taskSession.setEndTime(endTime);

    // Act
    long actualDurationInSeconds = taskSession.getDurationInSeconds();

    // Assert
    // Should be exactly 300 seconds (5 minutes)
    assertEquals(300, actualDurationInSeconds);
  }

  @Test
  @DisplayName("getDurationInSeconds_shouldReturnZero_whenStartAndEndTimeAreSame")
  void getDurationInSeconds_shouldReturnZero_whenStartAndEndTimeAreSame() {
    // Arrange
    LocalDateTime sameTime = LocalDateTime.now();
    taskSession.setStartTime(sameTime);
    taskSession.setEndTime(sameTime);

    // Act
    long actualDurationInSeconds = taskSession.getDurationInSeconds();

    // Assert
    assertEquals(0, actualDurationInSeconds);
  }

  @Test
  @DisplayName("endSession_shouldSetEndTime_whenSessionIsRunning")
  void endSession_shouldSetEndTime_whenSessionIsRunning() {
    // Arrange
    taskSession.setEndTime(null);
    LocalDateTime beforeEndTime = LocalDateTime.now();
    LocalDateTime endTime = LocalDateTime.now();

    // Act
    taskSession.endSession(endTime);

    // Assert
    assertNotNull(taskSession.getEndTime());
    assertFalse(taskSession.isRunning());
    assertTrue(taskSession.getEndTime().isAfter(beforeEndTime)
        || taskSession.getEndTime().isEqual(beforeEndTime));
  }

  @Test
  @DisplayName("endSession_shouldThrowException_whenEndTimeBeforeStartTime")
  void endSession_shouldThrowException_whenEndTimeBeforeStartTime() {
    // Arrange
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = LocalDateTime.now().minusMinutes(10); // End before start
    taskSession.setStartTime(startTime);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> taskSession.endSession(endTime)
    );
  }

  @Test
  @DisplayName("constructor_shouldInitializeWithNullEndTime")
  void constructor_shouldInitializeWithNullEndTime() {
    // Arrange & Act
    TaskSession newSession = new TaskSession();

    // Assert
    assertNull(newSession.getEndTime());
    assertTrue(newSession.isRunning());
  }

  @Test
  @DisplayName("setStartTime_shouldUpdateStartTime")
  void setStartTime_shouldUpdateStartTime() {
    // Arrange
    LocalDateTime newStartTime = LocalDateTime.now().minusHours(1);

    // Act
    taskSession.setStartTime(newStartTime);

    // Assert
    assertEquals(newStartTime, taskSession.getStartTime());
  }

  @Test
  @DisplayName("setTask_shouldUpdateTaskReference")
  void setTask_shouldUpdateTaskReference() {
    // Arrange
    Task newTask = new Task();
    newTask.setTaskName("New Task");

    // Act
    taskSession.setTask(newTask);

    // Assert
    assertEquals(newTask, taskSession.getTask());
    assertEquals(newTask.getTaskName(), taskSession.getTask().getTaskName());
  }
}
