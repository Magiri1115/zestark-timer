package com.zestark.timewatch.repository;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskEvent;
import com.zestark.timewatch.domain.model.TaskEventType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for TaskEvent entity.
 *
 * <p>Provides database access methods for task event-related operations.
 */
@Repository
public interface TaskEventRepository extends JpaRepository<TaskEvent, UUID> {

  /**
   * Finds all events belonging to a specific task.
   *
   * @param task the task whose events to retrieve
   * @return a list of events belonging to the task, ordered by occurrence time descending
   */
  @Query("SELECT te FROM TaskEvent te WHERE te.task = :task ORDER BY te.occurredAt DESC")
  List<TaskEvent> findByTaskOrderByOccurredAtDesc(@Param("task") Task task);

  /**
   * Finds all events of a specific type for a task.
   *
   * @param task the task whose events to retrieve
   * @param eventType the type of events to filter by
   * @return a list of events of the specified type for the task
   */
  List<TaskEvent> findByTaskAndEventType(Task task, TaskEventType eventType);
}
