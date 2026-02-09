'use client';

import { useTimer } from '@/hooks/useTimer';
import { formatDuration } from '@/lib/utils/time';

interface TaskTimerProps {
  taskId: string;
}

/**
 * Component for task timer control and display.
 */
export function TaskTimer({ taskId }: TaskTimerProps) {
  const {
    isTimerRunning,
    elapsedTimeInSeconds,
    isTimerLoading,
    timerError,
    startTimer,
    stopTimer,
  } = useTimer(taskId);

  const handleStartClick = async () => {
    try {
      await startTimer();
    } catch (error) {
      console.error('Failed to start timer:', error);
    }
  };

  const handleStopClick = async () => {
    try {
      await stopTimer();
    } catch (error) {
      console.error('Failed to stop timer:', error);
    }
  };

  return (
    <div className="border-t pt-4">
      <div className="text-3xl font-mono text-center mb-4">
        {formatDuration(elapsedTimeInSeconds)}
      </div>

      {timerError && (
        <div className="text-red-500 text-sm mb-2">{timerError}</div>
      )}

      <div className="flex gap-2 justify-center">
        {!isTimerRunning ? (
          <button
            onClick={handleStartClick}
            disabled={isTimerLoading}
            className="bg-green-500 text-white px-6 py-2 rounded hover:bg-green-600 disabled:bg-gray-300"
          >
            {isTimerLoading ? '開始中...' : '開始'}
          </button>
        ) : (
          <button
            onClick={handleStopClick}
            disabled={isTimerLoading}
            className="bg-red-500 text-white px-6 py-2 rounded hover:bg-red-600 disabled:bg-gray-300"
          >
            {isTimerLoading ? '停止中...' : '停止'}
          </button>
        )}
      </div>
    </div>
  );
}
