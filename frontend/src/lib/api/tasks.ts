import { apiRequest } from './client';
import type { Task, CreateTaskRequest, UpdateTaskRequest, TaskStatus } from '@/types';

/**
 * API client for task-related operations.
 */
export const taskApi = {
  /**
   * Creates a new task.
   *
   * @param userId the user ID
   * @param requestData the task creation request
   * @returns the created task
   */
  async createTask(userId: string, requestData: CreateTaskRequest): Promise<Task> {
    return apiRequest<Task>('POST', '/tasks', requestData, { userId });
  },

  /**
   * Gets all tasks for a user.
   *
   * @param userId the user ID
   * @returns a list of tasks
   */
  async getTasksByUser(userId: string): Promise<Task[]> {
    return apiRequest<Task[]>('GET', '/tasks', undefined, { userId });
  },

  /**
   * Gets a specific task by ID.
   *
   * @param taskId the task ID
   * @returns the task
   */
  async getTaskById(taskId: string): Promise<Task> {
    return apiRequest<Task>('GET', `/tasks/${taskId}`);
  },

  /**
   * Updates a task.
   *
   * @param taskId the task ID
   * @param requestData the task update request
   * @returns the updated task
   */
  async updateTask(taskId: string, requestData: UpdateTaskRequest): Promise<Task> {
    return apiRequest<Task>('PUT', `/tasks/${taskId}`, requestData);
  },

  /**
   * Changes a task's status.
   *
   * @param taskId the task ID
   * @param status the new status
   * @returns the updated task
   */
  async changeTaskStatus(taskId: string, status: TaskStatus): Promise<Task> {
    return apiRequest<Task>('PUT', `/tasks/${taskId}/status`, undefined, { status });
  },

  /**
   * Deletes a task.
   *
   * @param taskId the task ID
   */
  async deleteTask(taskId: string): Promise<void> {
    return apiRequest<void>('DELETE', `/tasks/${taskId}`);
  },
};
