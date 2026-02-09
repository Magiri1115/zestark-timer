package com.zestark.timewatch.service;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskEvent;
import com.zestark.timewatch.domain.model.TaskEventType;
import com.zestark.timewatch.domain.model.TaskSession;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.exception.ResourceNotFoundException;
import com.zestark.timewatch.exception.TaskSessionConflictException;
import com.zestark.timewatch.repository.TaskEventRepository;
import com.zestark.timewatch.repository.TaskRepository;
import com.zestark.timewatch.repository.TaskSessionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for TaskSession-related business logic.
 *
 * <p>Handles task session creation, timer start/stop operations, and session queries.
 * Ensures database-level exclusion control for concurrent session management.
 */
@Service
@Transactional
public class TaskSessionService {

  private final TaskSessionRepository taskSessionRepository;
  private final TaskRepository taskRepository;
  private final TaskEventRepository taskEventRepository;

  /**
   * Constructor for TaskSessionService.
   *
   * @param taskSessionRepository the task session repository
   * @param taskRepository the task repository
   * @param taskEventRepository the task event repository
   */
  public TaskSessionService(
      TaskSessionRepository taskSessionRepository,
      TaskRepository taskRepository,
      TaskEventRepository taskEventRepository) {
    this.taskSessionRepository = taskSessionRepository;
    this.taskRepository = taskRepository;
    this.taskEventRepository = taskEventRepository;
  }

  /**
   * Starts a new timer session for a task.
   *
   * <p>Updates task status to RUNNING and creates a START event.
   * Database-level exclusion control (UNIQUE INDEX WHERE end_time IS NULL)
   * prevents multiple running sessions for the same task.
   *
   * @param taskId the ID of the task to start
   * @return the created task session
   * @throws ResourceNotFoundException if task is not found
   * @throws TaskSessionConflictException if a session is already running for this task
   */
  public TaskSession startTaskSession(UUID taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

    Optional<TaskSession> runningSessionOptional =
        taskSessionRepository.findRunningSessionByTask(task);
    if (runningSessionOptional.isPresent()) {
      throw new TaskSessionConflictException(
          "Task already has a running session. Task ID: " + taskId
      );
    }

    LocalDateTime startTime = LocalDateTime.now();
    TaskSession newSession = new TaskSession(task, startTime);

    try {
      task.setStatus(TaskStatus.RUNNING);
      taskRepository.save(task);

      taskEventRepository.save(new TaskEvent(task, TaskEventType.START, startTime));

      TaskSession savedSession = taskSessionRepository.save(newSession);
      return savedSession;
    } catch (DataIntegrityViolationException exception) {
      throw new TaskSessionConflictException(
          "Failed to start session due to concurrent session conflict. Task ID: " + taskId,
          exception
      );
    }
  }

  /**
   * Stops the currently running session for a task.
   *
   * <p>Updates task status to PENDING and creates a STOP event.
   *
   * @param taskId the ID of the task to stop
   * @return the ended task session
   * @throws ResourceNotFoundException if task or running session is not found
   */
  public TaskSession stopTaskSession(UUID taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

    TaskSession runningSession = taskSessionRepository.findRunningSessionByTask(task)
        .orElseThrow(() -> new TaskSessionConflictException(
            "No running session found for task. Task ID: " + taskId
        ));

    LocalDateTime endTime = LocalDateTime.now();
    runningSession.endSession(endTime);

    task.setStatus(TaskStatus.PENDING);
    taskRepository.save(task);

    taskEventRepository.save(new TaskEvent(task, TaskEventType.STOP, endTime));

    TaskSession savedSession = taskSessionRepository.save(runningSession);
    return savedSession;
  }

  /**
   * Gets the currently running session for a task.
   *
   * @param taskId the ID of the task
   * @return an Optional containing the running session if found, empty otherwise
   * @throws ResourceNotFoundException if task is not found
   */
  @Transactional(readOnly = true)
  public Optional<TaskSession> getRunningSession(UUID taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

    return taskSessionRepository.findRunningSessionByTask(task);
  }

  /**
   * Gets all sessions for a task.
   *
   * @param taskId the ID of the task
   * @return a list of all sessions for the task
   * @throws ResourceNotFoundException if task is not found
   */
  @Transactional(readOnly = true)
  public List<TaskSession> getAllSessionsForTask(UUID taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

    return taskSessionRepository.findByTask(task);
  }

  /**
   * Gets all completed sessions for a task.
   *
   * @param taskId the ID of the task
   * @return a list of completed sessions for the task
   * @throws ResourceNotFoundException if task is not found
   */
  @Transactional(readOnly = true)
  public List<TaskSession> getCompletedSessionsForTask(UUID taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

    return taskSessionRepository.findCompletedSessionsByTask(task);
  }

  /**
   * Finds a task session by ID.
   *
   * @param sessionId the ID of the session to find
   * @return the found session
   * @throws ResourceNotFoundException if session is not found
   */
  @Transactional(readOnly = true)
  public TaskSession findSessionById(UUID sessionId) {
    return taskSessionRepository.findById(sessionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Task session not found with ID: " + sessionId
        ));
  }
}
