package com.zestark.timewatch.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TaskEvent entity representing an event that occurred during a task's lifecycle.
 *
 * <p>This entity maps to the 'task_events' table in the database.
 * Events track important state changes like start, stop, pause, resume, complete, and cancel.
 */
@Entity
@Table(name = "task_events")
public class TaskEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false, length = 50)
  private TaskEventType eventType;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime occurredAt;

  /**
   * Default constructor required by JPA.
   */
  public TaskEvent() {
    this.occurredAt = LocalDateTime.now();
  }

  /**
   * Constructor for creating a new task event with task and event type.
   *
   * @param task the task this event belongs to
   * @param eventType the type of event that occurred
   */
  public TaskEvent(Task task, TaskEventType eventType) {
    this();
    this.task = task;
    this.eventType = eventType;
  }

  /**
   * Constructor for creating a new task event with task, event type, and occurrence time.
   *
   * @param task the task this event belongs to
   * @param eventType the type of event that occurred
   * @param occurredAt the time when this event occurred
   */
  public TaskEvent(Task task, TaskEventType eventType, LocalDateTime occurredAt) {
    this.task = task;
    this.eventType = eventType;
    this.occurredAt = occurredAt;
  }

  public UUID getId() {
    return id;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public TaskEventType getEventType() {
    return eventType;
  }

  public void setEventType(TaskEventType eventType) {
    this.eventType = eventType;
  }

  public LocalDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(LocalDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }
}
