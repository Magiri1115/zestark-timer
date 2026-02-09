package com.zestark.timewatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.domain.model.User;
import com.zestark.timewatch.exception.ResourceNotFoundException;
import com.zestark.timewatch.repository.TaskEventRepository;
import com.zestark.timewatch.repository.TaskRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for TaskService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService単体テスト")
class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskEventRepository taskEventRepository;

  @InjectMocks
  private TaskService taskService;

  private User testUser;
  private Task testTask;

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
  }

  @Test
  @DisplayName("createTask_shouldReturnCreatedTask_whenValidDataProvided")
  void createTask_shouldReturnCreatedTask_whenValidDataProvided() {
    // Arrange
    String taskName = "New Task";
    String description = "New Description";
    when(taskRepository.save(any(Task.class))).thenReturn(testTask);

    // Act
    Task createdTask = taskService.createTask(testUser, taskName, description);

    // Assert
    assertNotNull(createdTask);
    verify(taskRepository).save(any(Task.class));
  }

  @Test
  @DisplayName("findTaskById_shouldReturnTask_whenTaskExists")
  void findTaskById_shouldReturnTask_whenTaskExists() {
    // Arrange
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));

    // Act
    Task foundTask = taskService.findTaskById(testTask.getId());

    // Assert
    assertNotNull(foundTask);
    assertEquals(testTask.getId(), foundTask.getId());
    assertEquals(testTask.getTaskName(), foundTask.getTaskName());
  }

  @Test
  @DisplayName("findTaskById_shouldThrowException_whenTaskNotFound")
  void findTaskById_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskService.findTaskById(nonExistentTaskId)
    );
  }

  @Test
  @DisplayName("getTasksByUser_shouldReturnTaskList_whenUserHasTasks")
  void getTasksByUser_shouldReturnTaskList_whenUserHasTasks() throws Exception {
    // Arrange
    Task anotherTask = new Task();
    setId(anotherTask, UUID.randomUUID());
    anotherTask.setUser(testUser);
    anotherTask.setTaskName("Another Task");
    anotherTask.setStatus(TaskStatus.PENDING);

    List<Task> expectedTasks = Arrays.asList(testTask, anotherTask);
    when(taskRepository.findByUser(testUser)).thenReturn(expectedTasks);

    // Act
    List<Task> actualTasks = taskService.getTasksByUser(testUser);

    // Assert
    assertNotNull(actualTasks);
    assertEquals(2, actualTasks.size());
    assertEquals(expectedTasks, actualTasks);
    verify(taskRepository).findByUser(testUser);
  }

  @Test
  @DisplayName("getTasksByUserAndStatus_shouldReturnFilteredList_whenCalled")
  void getTasksByUserAndStatus_shouldReturnFilteredList_whenCalled() throws Exception {
    // Arrange
    Task runningTask = new Task();
    setId(runningTask, UUID.randomUUID());
    runningTask.setUser(testUser);
    runningTask.setTaskName("Running Task");
    runningTask.setStatus(TaskStatus.RUNNING);

    List<Task> expectedTasks = Arrays.asList(runningTask);
    when(taskRepository.findByUserAndStatus(testUser, TaskStatus.RUNNING))
        .thenReturn(expectedTasks);

    // Act
    List<Task> actualTasks = taskService.getTasksByUserAndStatus(testUser, TaskStatus.RUNNING);

    // Assert
    assertNotNull(actualTasks);
    assertEquals(1, actualTasks.size());
    assertEquals(TaskStatus.RUNNING, actualTasks.get(0).getStatus());
    verify(taskRepository).findByUserAndStatus(testUser, TaskStatus.RUNNING);
  }

  @Test
  @DisplayName("updateTask_shouldReturnUpdatedTask_whenValidDataProvided")
  void updateTask_shouldReturnUpdatedTask_whenValidDataProvided() {
    // Arrange
    String newTaskName = "Updated Task";
    String newDescription = "Updated Description";
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskRepository.save(any(Task.class))).thenReturn(testTask);

    // Act
    Task updatedTask = taskService.updateTask(testTask.getId(), newTaskName, newDescription);

    // Assert
    assertNotNull(updatedTask);
    verify(taskRepository).save(testTask);
  }

  @Test
  @DisplayName("updateTask_shouldThrowException_whenTaskNotFound")
  void updateTask_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskService.updateTask(nonExistentTaskId, "New Name", "New Desc")
    );
  }

  @Test
  @DisplayName("changeTaskStatus_shouldUpdateStatus_whenValidStatusProvided")
  void changeTaskStatus_shouldUpdateStatus_whenValidStatusProvided() {
    // Arrange
    TaskStatus newStatus = TaskStatus.COMPLETED;
    when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));
    when(taskRepository.save(any(Task.class))).thenReturn(testTask);

    // Act
    Task updatedTask = taskService.changeTaskStatus(testTask.getId(), newStatus);

    // Assert
    assertNotNull(updatedTask);
    verify(taskRepository).save(testTask);
    verify(taskEventRepository).save(any());
  }

  @Test
  @DisplayName("changeTaskStatus_shouldThrowException_whenTaskNotFound")
  void changeTaskStatus_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskService.changeTaskStatus(nonExistentTaskId, TaskStatus.COMPLETED)
    );
  }

  @Test
  @DisplayName("deleteTask_shouldDeleteTask_whenTaskExists")
  void deleteTask_shouldDeleteTask_whenTaskExists() {
    // Arrange
    when(taskRepository.existsById(testTask.getId())).thenReturn(true);

    // Act
    taskService.deleteTask(testTask.getId());

    // Assert
    verify(taskRepository).deleteById(testTask.getId());
  }

  @Test
  @DisplayName("deleteTask_shouldThrowException_whenTaskNotFound")
  void deleteTask_shouldThrowException_whenTaskNotFound() {
    // Arrange
    UUID nonExistentTaskId = UUID.randomUUID();
    when(taskRepository.existsById(nonExistentTaskId)).thenReturn(false);

    // Act & Assert
    assertThrows(
        ResourceNotFoundException.class,
        () -> taskService.deleteTask(nonExistentTaskId)
    );
  }
}
