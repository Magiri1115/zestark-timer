import { useState, useEffect } from 'react';
import { taskApi } from '@/lib/api';
import type { Task, CreateTaskRequest, UpdateTaskRequest, TaskStatus } from '@/types';

/**
 * Custom hook for task management.
 *
 * @param userId the user ID
 * @returns task management functions and state
 */
export function useTasks(userId: string) {
  const [taskList, setTaskList] = useState<Task[]>([]);
  const [isTaskListLoading, setIsTaskListLoading] = useState(false);
  const [taskListError, setTaskListError] = useState<string | null>(null);

  /**
   * Fetches all tasks for the user.
   */
  const fetchTasks = async () => {
    setIsTaskListLoading(true);
    setTaskListError(null);

    try {
      const fetchedTaskList = await taskApi.getTasksByUser(userId);
      setTaskList(fetchedTaskList);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch tasks';
      setTaskListError(errorMessage);
    } finally {
      setIsTaskListLoading(false);
    }
  };

  /**
   * Creates a new task.
   *
   * @param requestData the task creation request
   */
  const createTask = async (requestData: CreateTaskRequest) => {
    try {
      const createdTask = await taskApi.createTask(userId, requestData);
      setTaskList((previousTaskList) => [...previousTaskList, createdTask]);
      return createdTask;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to create task';
      setTaskListError(errorMessage);
      throw error;
    }
  };

  /**
   * Updates a task.
   *
   * @param taskId the task ID
   * @param requestData the task update request
   */
  const updateTask = async (taskId: string, requestData: UpdateTaskRequest) => {
    try {
      const updatedTask = await taskApi.updateTask(taskId, requestData);
      setTaskList((previousTaskList) =>
        previousTaskList.map((task) => (task.id === taskId ? updatedTask : task))
      );
      return updatedTask;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to update task';
      setTaskListError(errorMessage);
      throw error;
    }
  };

  /**
   * Changes a task's status.
   *
   * @param taskId the task ID
   * @param status the new status
   */
  const changeTaskStatus = async (taskId: string, status: TaskStatus) => {
    try {
      const updatedTask = await taskApi.changeTaskStatus(taskId, status);
      setTaskList((previousTaskList) =>
        previousTaskList.map((task) => (task.id === taskId ? updatedTask : task))
      );
      return updatedTask;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to change task status';
      setTaskListError(errorMessage);
      throw error;
    }
  };

  /**
   * Deletes a task.
   *
   * @param taskId the task ID
   */
  const deleteTask = async (taskId: string) => {
    try {
      await taskApi.deleteTask(taskId);
      setTaskList((previousTaskList) => previousTaskList.filter((task) => task.id !== taskId));
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to delete task';
      setTaskListError(errorMessage);
      throw error;
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [userId]);

  return {
    taskList,
    isTaskListLoading,
    taskListError,
    fetchTasks,
    createTask,
    updateTask,
    changeTaskStatus,
    deleteTask,
  };
}
