import { apiRequest } from './client';
import type { TaskSession } from '@/types';

/**
 * API client for task session-related operations.
 */
export const sessionApi = {
  /**
   * Starts a timer session for a task.
   *
   * @param taskId the task ID
   * @returns the created session
   */
  async startSession(taskId: string): Promise<TaskSession> {
    return apiRequest<TaskSession>('POST', `/tasks/${taskId}/sessions/start`);
  },

  /**
   * Stops the currently running session for a task.
   *
   * @param taskId the task ID
   * @returns the ended session
   */
  async stopSession(taskId: string): Promise<TaskSession> {
    return apiRequest<TaskSession>('POST', `/tasks/${taskId}/sessions/stop`);
  },

  /**
   * Gets the currently running session for a task.
   *
   * @param taskId the task ID
   * @returns the running session, or null if no session is running
   */
  async getRunningSession(taskId: string): Promise<TaskSession | null> {
    try {
      return await apiRequest<TaskSession>('GET', `/tasks/${taskId}/sessions/running`);
    } catch (error: unknown) {
      const apiError = error as { status?: number };
      if (apiError.status === 404) {
        return null;
      }
      throw error;
    }
  },

  /**
   * Gets all sessions for a task.
   *
   * @param taskId the task ID
   * @returns a list of sessions
   */
  async getAllSessions(taskId: string): Promise<TaskSession[]> {
    return apiRequest<TaskSession[]>('GET', `/tasks/${taskId}/sessions`);
  },

  /**
   * Gets all completed sessions for a task.
   *
   * @param taskId the task ID
   * @returns a list of completed sessions
   */
  async getCompletedSessions(taskId: string): Promise<TaskSession[]> {
    return apiRequest<TaskSession[]>('GET', `/tasks/${taskId}/sessions/completed`);
  },

  /**
   * Gets a specific session by ID.
   *
   * @param taskId the task ID
   * @param sessionId the session ID
   * @returns the session
   */
  async getSessionById(taskId: string, sessionId: string): Promise<TaskSession> {
    return apiRequest<TaskSession>('GET', `/tasks/${taskId}/sessions/${sessionId}`);
  },
};
