package com.zestark.timewatch.controller;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.domain.model.User;
import com.zestark.timewatch.dto.CreateTaskRequest;
import com.zestark.timewatch.dto.TaskResponse;
import com.zestark.timewatch.service.TaskService;
import com.zestark.timewatch.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Task-related endpoints.
 *
 * <p>Provides APIs for task CRUD operations and status management.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;
  private final UserService userService;

  public TaskController(TaskService taskService, UserService userService) {
    this.taskService = taskService;
    this.userService = userService;
  }

  /**
   * Creates a new task.
   *
   * @param userId the ID of the user creating the task
   * @param requestDto the task creation request
   * @return the created task
   */
  @PostMapping
  public ResponseEntity<TaskResponse> createTask(
      @RequestParam UUID userId,
      @Valid @RequestBody CreateTaskRequest requestDto) {
    User user = userService.findUserById(userId);
    Task createdTask = taskService.createTask(
        user,
        requestDto.getTaskName(),
        requestDto.getDescription()
    );
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TaskResponse.fromEntity(createdTask));
  }

  /**
   * Gets all tasks for a user.
   *
   * @param userId the ID of the user
   * @return a list of tasks belonging to the user
   */
  @GetMapping
  public ResponseEntity<List<TaskResponse>> getTasksByUser(@RequestParam UUID userId) {
    User user = userService.findUserById(userId);
    List<Task> taskList = taskService.getTasksByUser(user);
    List<TaskResponse> responseList = taskList.stream()
        .map(TaskResponse::fromEntity)
        .toList();
    return ResponseEntity.ok(responseList);
  }

  /**
   * Gets a specific task by ID.
   *
   * @param taskId the ID of the task
   * @return the task
   */
  @GetMapping("/{taskId}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID taskId) {
    Task task = taskService.findTaskById(taskId);
    return ResponseEntity.ok(TaskResponse.fromEntity(task));
  }

  /**
   * Updates a task's name and description.
   *
   * @param taskId the ID of the task to update
   * @param requestDto the update request
   * @return the updated task
   */
  @PutMapping("/{taskId}")
  public ResponseEntity<TaskResponse> updateTask(
      @PathVariable UUID taskId,
      @Valid @RequestBody CreateTaskRequest requestDto) {
    Task updatedTask = taskService.updateTask(
        taskId,
        requestDto.getTaskName(),
        requestDto.getDescription()
    );
    return ResponseEntity.ok(TaskResponse.fromEntity(updatedTask));
  }

  /**
   * Changes a task's status.
   *
   * @param taskId the ID of the task
   * @param status the new status
   * @return the updated task
   */
  @PutMapping("/{taskId}/status")
  public ResponseEntity<TaskResponse> changeTaskStatus(
      @PathVariable UUID taskId,
      @RequestParam TaskStatus status) {
    Task updatedTask = taskService.changeTaskStatus(taskId, status);
    return ResponseEntity.ok(TaskResponse.fromEntity(updatedTask));
  }

  /**
   * Deletes a task.
   *
   * @param taskId the ID of the task to delete
   * @return no content response
   */
  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
    taskService.deleteTask(taskId);
    return ResponseEntity.noContent().build();
  }
}
