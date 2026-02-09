/**
 * Task status enum matching backend TaskStatus.
 */
export enum TaskStatus {
  PENDING = 'PENDING',
  RUNNING = 'RUNNING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

/**
 * Task entity type matching backend Task entity.
 */
export interface Task {
  id: string;
  userId: string;
  taskName: string;
  description: string | null;
  status: TaskStatus;
  createdAt: string;
  updatedAt: string;
}

/**
 * Request type for creating a new task.
 */
export interface CreateTaskRequest {
  taskName: string;
  description?: string;
}

/**
 * Request type for updating a task.
 */
export interface UpdateTaskRequest {
  taskName: string;
  description?: string;
}
