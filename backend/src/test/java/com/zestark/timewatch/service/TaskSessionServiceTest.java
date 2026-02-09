package com.zestark.timewatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskEvent;
import com.zestark.timewatch.domain.model.TaskSession;
import com.zestark.timewatch.domain.model.User;
import com.zestark.timewatch.domain.model.TaskEventType;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.exception.ResourceNotFoundException;
import com.zestark.timewatch.exception.TaskSessionConflictException;
import com.zestark.timewatch.repository.TaskEventRepository;
import com.zestark.timewatch.repository.TaskRepository;
import com.zestark.timewatch.repository.TaskSessionRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Unit tests for TaskSessionService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskSessionService単体テスト")
class TaskSessionServiceTest {

  @Mock
  private TaskSessionRepository taskSessionRepository;

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskEventRepository taskEventRepository;

  @InjectMocks
  private TaskSessionService taskSessionService;

  private User testUser;
  private Task testTask;
  private TaskSession testSession;

  /**
   * Helper method to set ID field using reflection.
   */
  private void setId(Object entity, UUID id) throws Exception {
    Field idField = entity.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(entity, id);
  }

  /**
   * Setup test data before each test.
   */
  @BeforeEach
  void setUp() throws Exception {
    testUser = new User();
    setId(testUser, UUID.randomUUID());
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");

    testTask = new Task();
    setId(testTask, UUID.randomUUID());
    testTask.setUser(testUser);
    testTask.setTaskName("Test Task");
    testTask.setDescription("Test Description");
    testTask.setStatus(TaskStatus.PENDING);

    testSession = new TaskSession();
    setId(testSession, UUID.randomUUID());
    testSession.setTask(testTask);
    testSession.setStartTime(LocalDateTime.now().minusMinutes(10));
  }

  @Test
  @DisplayName("startTaskSession_shouldCreateNewSession_whenNoRunningSessionExists")
  void startTaskSession_shouldCreateNewSession_whenNoRunningSessionExists() {
    // Arrange
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.empty());
    when(taskSessionRepository.save(any(TaskSession.class))).thenReturn(testSession);

    // Act
    TaskSession createdSession = taskSessionService.startTaskSession(testTask.getId());

    // Assert
    assertNotNull(createdSession);
    assertEquals(testTask.getId(), createdSession.getTask().getId());
    assertTrue(createdSession.isRunning());
    assertEquals(TaskStatus.RUNNING, testTask.getStatus());
    verify(taskRepository).save(testTask);
    verify(taskEventRepository).save(any(TaskEvent.class));
    verify(taskSessionRepository).save(any(TaskSession.class));
  }

  @Test
  @DisplayName("startTaskSession_shouldThrowException_whenTaskNotFound")
  void startTaskSession_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskSessionService.startTaskSession(nonExistentTaskId)
    );
  }

  @Test
  @DisplayName("startTaskSession_shouldThrowException_whenRunningSessionAlreadyExists")
  void startTaskSession_shouldThrowException_whenRunningSessionAlreadyExists() throws Exception {
    // Arrange
    TaskSession existingRunningSession = new TaskSession();
    setId(existingRunningSession, UUID.randomUUID());
    existingRunningSession.setTask(testTask);
    existingRunningSession.setStartTime(LocalDateTime.now().minusMinutes(5));

    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.of(existingRunningSession));

    // Act & Assert
    assertThrows(
        TaskSessionConflictException.class,
        () -> taskSessionService.startTaskSession(testTask.getId())
    );
  }

  @Test
  @DisplayName("startTaskSession_shouldThrowException_whenDatabaseConstraintViolated")
  void startTaskSession_shouldThrowException_whenDatabaseConstraintViolated() {
    // Arrange
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.empty());
    when(taskSessionRepository.save(any(TaskSession.class)))
        .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

    // Act & Assert
    assertThrows(
        TaskSessionConflictException.class,
        () -> taskSessionService.startTaskSession(testTask.getId())
    );
  }

  @Test
  @DisplayName("stopTaskSession_shouldEndSession_whenRunningSessionExists")
  void stopTaskSession_shouldEndSession_whenRunningSessionExists() {
    // Arrange
    testSession.setEndTime(null); // Running session
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.of(testSession));
    when(taskSessionRepository.save(any(TaskSession.class))).thenReturn(testSession);

    // Act
    TaskSession stoppedSession = taskSessionService.stopTaskSession(testTask.getId());

    // Assert
    assertNotNull(stoppedSession);
    assertNotNull(stoppedSession.getEndTime());
    assertFalse(stoppedSession.isRunning());
    assertEquals(TaskStatus.PENDING, testTask.getStatus());
    verify(taskRepository).save(testTask);
    verify(taskEventRepository).save(any(TaskEvent.class));
    verify(taskSessionRepository).save(testSession);
  }

  @Test
  @DisplayName("stopTaskSession_shouldThrowException_whenTaskNotFound")
  void stopTaskSession_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskSessionService.stopTaskSession(nonExistentTaskId)
    );
  }

  @Test
  @DisplayName("stopTaskSession_shouldThrowException_whenNoRunningSessionExists")
  void stopTaskSession_shouldThrowException_whenNoRunningSessionExists() {
    // Arrange
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        TaskSessionConflictException.class,
        () -> taskSessionService.stopTaskSession(testTask.getId())
    );
  }

  @Test
  @DisplayName("getRunningSession_shouldReturnSession_whenRunningSessionExists")
  void getRunningSession_shouldReturnSession_whenRunningSessionExists() {
    // Arrange
    testSession.setEndTime(null); // Running session
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.of(testSession));

    // Act
    Optional<TaskSession> runningSession =
        taskSessionService.getRunningSession(testTask.getId());

    // Assert
    assertTrue(runningSession.isPresent());
    assertEquals(testSession.getId(), runningSession.get().getId());
    assertTrue(runningSession.get().isRunning());
  }

  @Test
  @DisplayName("getRunningSession_shouldReturnEmpty_whenNoRunningSessionExists")
  void getRunningSession_shouldReturnEmpty_whenNoRunningSessionExists() {
    // Arrange
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskSessionRepository.findRunningSessionByTask(testTask))
        .thenReturn(Optional.empty());

    // Act
    Optional<TaskSession> runningSession =
        taskSessionService.getRunningSession(testTask.getId());

    // Assert
    assertFalse(runningSession.isPresent());
  }

  @Test
  @DisplayName("getRunningSession_shouldThrowException_whenTaskNotFound")
  void getRunningSession_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskSessionService.getRunningSession(nonExistentTaskId)
    );
  }

  @Test
  @DisplayName("findSessionById_shouldReturnSession_whenSessionExists")
  void findSessionById_shouldReturnSession_whenSessionExists() {
    // Arrange
    when(taskSessionRepository.findById(testSession.getId()))
        .thenReturn(Optional.of(testSession));

    // Act
    TaskSession foundSession = taskSessionService.findSessionById(testSession.getId());

    // Assert
    assertNotNull(foundSession);
    assertEquals(testSession.getId(), foundSession.getId());
  }

  @Test
  @DisplayName("findSessionById_shouldThrowException_whenSessionNotFound")
  void findSessionById_shouldThrowException_whenSessionNotFound() {
    // Arrange
    UUID nonExistentSessionId = UUID.randomUUID();
    when(taskSessionRepository.findById(nonExistentSessionId))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskSessionService.findSessionById(nonExistentSessionId)
    );
  }
}
