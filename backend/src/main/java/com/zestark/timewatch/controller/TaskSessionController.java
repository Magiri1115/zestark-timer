package com.zestark.timewatch.controller;

import com.zestark.timewatch.domain.model.TaskSession;
import com.zestark.timewatch.dto.TaskSessionResponse;
import com.zestark.timewatch.service.TaskSessionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for TaskSession-related endpoints.
 *
 * <p>Provides APIs for task timer start/stop operations and session queries.
 */
@RestController
@RequestMapping("/tasks/{taskId}/sessions")
public class TaskSessionController {

  private final TaskSessionService taskSessionService;

  public TaskSessionController(TaskSessionService taskSessionService) {
    this.taskSessionService = taskSessionService;
  }

  /**
   * Starts a timer session for a task.
   *
   * @param taskId the ID of the task
   * @return the created task session
   */
  @PostMapping("/start")
  public ResponseEntity<TaskSessionResponse> startTaskSession(@PathVariable UUID taskId) {
    TaskSession startedSession = taskSessionService.startTaskSession(taskId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TaskSessionResponse.fromEntity(startedSession));
  }

  /**
   * Stops the currently running session for a task.
   *
   * @param taskId the ID of the task
   * @return the ended task session
   */
  @PostMapping("/stop")
  public ResponseEntity<TaskSessionResponse> stopTaskSession(@PathVariable UUID taskId) {
    TaskSession stoppedSession = taskSessionService.stopTaskSession(taskId);
    return ResponseEntity.ok(TaskSessionResponse.fromEntity(stoppedSession));
  }

  /**
   * Gets the currently running session for a task.
   *
   * @param taskId the ID of the task
   * @return the running session, or 404 if no session is running
   */
  @GetMapping("/running")
  public ResponseEntity<TaskSessionResponse> getRunningSession(@PathVariable UUID taskId) {
    Optional<TaskSession> runningSessionOptional = taskSessionService.getRunningSession(taskId);
    return runningSessionOptional
        .map(session -> ResponseEntity.ok(TaskSessionResponse.fromEntity(session)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Gets all sessions for a task.
   *
   * @param taskId the ID of the task
   * @return a list of all sessions for the task
   */
  @GetMapping
  public ResponseEntity<List<TaskSessionResponse>> getAllSessions(@PathVariable UUID taskId) {
    List<TaskSession> sessionList = taskSessionService.getAllSessionsForTask(taskId);
    List<TaskSessionResponse> responseList = sessionList.stream()
        .map(TaskSessionResponse::fromEntity)
        .toList();
    return ResponseEntity.ok(responseList);
  }

  /**
   * Gets all completed sessions for a task.
   *
   * @param taskId the ID of the task
   * @return a list of completed sessions for the task
   */
  @GetMapping("/completed")
  public ResponseEntity<List<TaskSessionResponse>> getCompletedSessions(
      @PathVariable UUID taskId) {
    List<TaskSession> completedSessionList =
        taskSessionService.getCompletedSessionsForTask(taskId);
    List<TaskSessionResponse> responseList = completedSessionList.stream()
        .map(TaskSessionResponse::fromEntity)
        .toList();
    return ResponseEntity.ok(responseList);
  }

  /**
   * Gets a specific session by ID.
   *
   * @param taskId the ID of the task (for path consistency)
   * @param sessionId the ID of the session
   * @return the session
   */
  @GetMapping("/{sessionId}")
  public ResponseEntity<TaskSessionResponse> getSessionById(
      @PathVariable UUID taskId,
      @PathVariable UUID sessionId) {
    TaskSession taskSession = taskSessionService.findSessionById(sessionId);
    return ResponseEntity.ok(TaskSessionResponse.fromEntity(taskSession));
  }
}
