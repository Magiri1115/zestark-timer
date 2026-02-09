package com.zestark.timewatch.repository;

import com.zestark.timewatch.domain.model.Task;
import com.zestark.timewatch.domain.model.TaskStatus;
import com.zestark.timewatch.domain.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Task entity.
 *
 * <p>Provides database access methods for task-related operations.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

  /**
   * Finds all tasks belonging to a specific user.
   *
   * @param user the user whose tasks to retrieve
   * @return a list of tasks belonging to the user
   */
  List<Task> findByUser(User user);

  /**
   * Finds all tasks belonging to a specific user with a specific status.
   *
   * @param user the user whose tasks to retrieve
   * @param status the status to filter by
   * @return a list of tasks belonging to the user with the specified status
   */
  List<Task> findByUserAndStatus(User user, TaskStatus status);

  /**
   * Finds all tasks with a specific status.
   *
   * @param status the status to filter by
   * @return a list of tasks with the specified status
   */
  List<Task> findByStatus(TaskStatus status);
}
