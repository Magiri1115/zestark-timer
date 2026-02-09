package com.zestark.timewatch.repository;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for TaskSession entity.
 *
 * <p>Provides database access methods for task session-related operations.
 */
@Repository
public interface TaskSessionRepository extends JpaRepository<TaskSession, UUID> {

  /**
   * Finds all sessions belonging to a specific task.
   *
   * @param task the task whose sessions to retrieve
   * @return a list of sessions belonging to the task
   */
  List<TaskSession> findByTask(Task task);

  /**
   * Finds the currently running session for a task (end_time IS NULL).
   *
   * <p>Due to database-level exclusion control (UNIQUE INDEX WHERE end_time IS NULL),
   * there can be at most one running session per task.
   *
   * @param task the task to check for a running session
   * @return an Optional containing the running session if found, empty otherwise
   */
  @Query("SELECT ts FROM TaskSession ts WHERE ts.task = :task AND ts.endTime IS NULL")
  Optional<TaskSession> findRunningSessionByTask(@Param("task") Task task);

  /**
   * Finds all completed sessions for a task (end_time IS NOT NULL).
   *
   * @param task the task whose completed sessions to retrieve
   * @return a list of completed sessions for the task
   */
  @Query("SELECT ts FROM TaskSession ts WHERE ts.task = :task AND ts.endTime IS NOT NULL "
      + "ORDER BY ts.startTime DESC")
  List<TaskSession> findCompletedSessionsByTask(@Param("task") Task task);
}
