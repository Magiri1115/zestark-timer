/**
 * Task session type matching backend TaskSession entity.
 */
export interface TaskSession {
  id: string;
  taskId: string;
  startTime: string;
  endTime: string | null;
  durationInSeconds: number;
  isRunning: boolean;
  createdAt: string;
}

/**
 * Timer state for local management.
 */
export interface TimerState {
  taskId: string | null;
  startTime: number | null;
  isRunning: boolean;
  elapsedTimeInSeconds: number;
}
