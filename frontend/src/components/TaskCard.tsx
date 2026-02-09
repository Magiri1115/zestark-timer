'use client';

import type { Task } from '@/types';
import { TaskTimer } from './TaskTimer';

interface TaskCardProps {
  task: Task;
  onDelete: () => void;
}

/**
 * Component displaying a single task card with timer.
 */
export function TaskCard({ task, onDelete }: TaskCardProps) {
  const statusColors = {
    PENDING: 'bg-gray-200 text-gray-800',
    RUNNING: 'bg-green-200 text-green-800',
    COMPLETED: 'bg-blue-200 text-blue-800',
    CANCELLED: 'bg-red-200 text-red-800',
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <div className="flex justify-between items-start mb-4">
        <div className="flex-1">
          <h3 className="text-xl font-bold mb-2">{task.taskName}</h3>
          {task.description && (
            <p className="text-gray-600 mb-2">{task.description}</p>
          )}
          <span
            className={`inline-block px-3 py-1 rounded-full text-sm ${
              statusColors[task.status]
            }`}
          >
            {task.status}
          </span>
        </div>

        <button
          onClick={onDelete}
          className="text-red-500 hover:text-red-700"
          title="削除"
        >
          ✕
        </button>
      </div>

      <TaskTimer taskId={task.id} />
    </div>
  );
}
