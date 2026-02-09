'use client';

import { useState } from 'react';
import { useTasks } from '@/hooks/useTasks';
import { TaskList } from '@/components/TaskList';
import { CreateTaskForm } from '@/components/CreateTaskForm';

/**
 * Home page component showing task list and creation form.
 */
export default function HomePage() {
  // Use the test user ID from the database
  const DEMO_USER_ID = 'e7daaa3e-6c32-41c1-8647-cd217acbc164';
  const [isCreatingTask, setIsCreatingTask] = useState(false);

  const {
    taskList,
    isTaskListLoading,
    taskListError,
    createTask,
    deleteTask,
  } = useTasks(DEMO_USER_ID);

  const handleCreateTask = async (taskName: string, description: string) => {
    try {
      await createTask({ taskName, description });
      setIsCreatingTask(false);
    } catch (error) {
      console.error('Failed to create task:', error);
    }
  };

  return (
    <main className="min-h-screen p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold mb-8">Zestark Timer</h1>

        <div className="mb-6">
          {!isCreatingTask ? (
            <button
              onClick={() => setIsCreatingTask(true)}
              className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600"
            >
              新しいタスクを作成
            </button>
          ) : (
            <CreateTaskForm
              onSubmit={handleCreateTask}
              onCancel={() => setIsCreatingTask(false)}
            />
          )}
        </div>

        {taskListError && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            エラー: {taskListError}
          </div>
        )}

        {isTaskListLoading ? (
          <div className="text-center py-8">読み込み中...</div>
        ) : (
          <TaskList taskList={taskList} onDeleteTask={deleteTask} />
        )}
      </div>
    </main>
  );
}
