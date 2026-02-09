'use client';

import type { Task } from '@/types';
import { TaskCard } from './TaskCard';

interface TaskListProps {
  taskList: Task[];
  onDeleteTask: (taskId: string) => void;
}

/**
 * Component displaying a list of tasks.
 */
export function TaskList({ taskList, onDeleteTask }: TaskListProps) {
  if (taskList.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        タスクがありません。新しいタスクを作成してください。
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {taskList.map((task) => (
        <TaskCard key={task.id} task={task} onDelete={() => onDeleteTask(task.id)} />
      ))}
    </div>
  );
}
