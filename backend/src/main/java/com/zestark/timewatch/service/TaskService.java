package com.zestark.timewatch.service;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskEvent;
import com.zestark.timewatch.domain.model.TaskEventType;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.domain.model.User;
import com.zestark.timewatch.exception.ResourceNotFoundException;
import com.zestark.timewatch.repository.TaskEventRepository;
import com.zestark.timewatch.repository.TaskRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for Task-related business logic.
 *
 * <p>Handles task creation, retrieval, updates, and status management.
 */
@Service
@Transactional
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskEventRepository taskEventRepository;

  public TaskService(TaskRepository taskRepository, TaskEventRepository taskEventRepository) {
    this.taskRepository = taskRepository;
    this.taskEventRepository = taskEventRepository;
  }

  /**
   * Creates a new task for a user.
   *
   * @param user the user who owns the task
   * @param taskName the name of the task
   * @param description optional description of the task
   * @return the created task
   */
  public Task createTask(User user, String taskName, String description) {
    Task newTask = new Task(user, taskName, description);
    Task savedTask = taskRepository.save(newTask);
    return savedTask;
  }

  /**
   * Finds a task by ID.
   *
   * @param taskId the ID of the task to find
   * @return the found task
   * @throws ResourceNotFoundException if task is not found
   */
  @Transactional(readOnly = true)
  public Task findTaskById(UUID taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
  }

  /**
   * Gets all tasks for a user.
   *
   * @param user the user whose tasks to retrieve
   * @return a list of tasks belonging to the user
   */
  @Transactional(readOnly = true)
  public List<Task> getTasksByUser(User user) {
    return taskRepository.findByUser(user);
  }

  /**
   * Gets all tasks for a user with a specific status.
   *
   * @param user the user whose tasks to retrieve
   * @param status the status to filter by
   * @return a list of tasks belonging to the user with the specified status
   */
  @Transactional(readOnly = true)
  public List<Task> getTasksByUserAndStatus(User user, TaskStatus status) {
    return taskRepository.findByUserAndStatus(user, status);
  }

  /**
   * Updates a task's name and description.
   *
   * @param taskId the ID of the task to update
   * @param newTaskName the new name for the task
   * @param newDescription the new description for the task
   * @return the updated task
   * @throws ResourceNotFoundException if task is not found
   */
  public Task updateTask(UUID taskId, String newTaskName, String newDescription) {
    Task existingTask = findTaskById(taskId);
    existingTask.setTaskName(newTaskName);
    existingTask.setDescription(newDescription);
    return taskRepository.save(existingTask);
  }

  /**
   * Changes a task's status and records an event.
   *
   * @param taskId the ID of the task to update
   * @param newStatus the new status for the task
   * @return the updated task
   * @throws ResourceNotFoundException if task is not found
   */
  public Task changeTaskStatus(UUID taskId, TaskStatus newStatus) {
    Task existingTask = findTaskById(taskId);
    TaskStatus oldStatus = existingTask.getStatus();
    existingTask.setStatus(newStatus);
    Task savedTask = taskRepository.save(existingTask);

    TaskEventType eventType = mapStatusToEventType(newStatus);
    if (eventType != null) {
      taskEventRepository.save(new TaskEvent(savedTask, eventType));
    }

    return savedTask;
  }

  /**
   * Deletes a task.
   *
   * @param taskId the ID of the task to delete
   * @throws ResourceNotFoundException if task is not found
   */
  public void deleteTask(UUID taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new ResourceNotFoundException("Task not found with ID: " + taskId);
    }
    taskRepository.deleteById(taskId);
  }

  private TaskEventType mapStatusToEventType(TaskStatus status) {
    return switch (status) {
      case COMPLETED -> TaskEventType.COMPLETE;
      case CANCELLED -> TaskEventType.CANCEL;
      default -> null;
    };
  }
}
