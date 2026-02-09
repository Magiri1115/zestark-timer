import { useState, useEffect, useRef } from 'react';
import { sessionApi } from '@/lib/api';
import { calculateElapsedTimeInSeconds } from '@/lib/utils/time';
import type { TaskSession } from '@/types';

/**
 * Custom hook for timer management.
 *
 * <p>Uses OS time-based calculation for accuracy, ensuring timer precision
 * even during background execution or device sleep.
 *
 * @param taskId the task ID
 * @returns timer management functions and state
 */
export function useTimer(taskId: string) {
  const [currentSession, setCurrentSession] = useState<TaskSession | null>(null);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [elapsedTimeInSeconds, setElapsedTimeInSeconds] = useState(0);
  const [isTimerLoading, setIsTimerLoading] = useState(false);
  const [timerError, setTimerError] = useState<string | null>(null);

  const intervalIdRef = useRef<NodeJS.Timeout | null>(null);

  /**
   * Starts the timer for a task.
   */
  const startTimer = async () => {
    setIsTimerLoading(true);
    setTimerError(null);

    try {
      const startedSession = await sessionApi.startSession(taskId);
      setCurrentSession(startedSession);
      setIsTimerRunning(true);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to start timer';
      setTimerError(errorMessage);
      throw error;
    } finally {
      setIsTimerLoading(false);
    }
  };

  /**
   * Stops the timer for a task.
   */
  const stopTimer = async () => {
    setIsTimerLoading(true);
    setTimerError(null);

    try {
      const stoppedSession = await sessionApi.stopSession(taskId);
      setCurrentSession(stoppedSession);
      setIsTimerRunning(false);
      setElapsedTimeInSeconds(stoppedSession.durationInSeconds);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to stop timer';
      setTimerError(errorMessage);
      throw error;
    } finally {
      setIsTimerLoading(false);
    }
  };

  /**
   * Fetches the currently running session.
   */
  const fetchRunningSession = async () => {
    try {
      const runningSession = await sessionApi.getRunningSession(taskId);
      if (runningSession) {
        setCurrentSession(runningSession);
        setIsTimerRunning(true);
      } else {
        setCurrentSession(null);
        setIsTimerRunning(false);
        setElapsedTimeInSeconds(0);
      }
    } catch (error) {
      console.error('Failed to fetch running session:', error);
    }
  };

  /**
   * Updates elapsed time based on OS time calculation.
   */
  useEffect(() => {
    if (isTimerRunning && currentSession) {
      // Parse ISO string as UTC by appending 'Z' if not present
      const startTimeString = currentSession.startTime.endsWith('Z')
        ? currentSession.startTime
        : currentSession.startTime + 'Z';
      const startTimeMs = new Date(startTimeString).getTime();

      const updateElapsedTime = () => {
        const calculatedElapsedTime = calculateElapsedTimeInSeconds(startTimeMs);
        setElapsedTimeInSeconds(calculatedElapsedTime);
      };

      updateElapsedTime();

      intervalIdRef.current = setInterval(updateElapsedTime, 1000);

      return () => {
        if (intervalIdRef.current) {
          clearInterval(intervalIdRef.current);
        }
      };
    } else {
      if (intervalIdRef.current) {
        clearInterval(intervalIdRef.current);
      }
    }
  }, [isTimerRunning, currentSession]);

  /**
   * Fetches running session on mount.
   */
  useEffect(() => {
    fetchRunningSession();
  }, [taskId]);

  return {
    currentSession,
    isTimerRunning,
    elapsedTimeInSeconds,
    isTimerLoading,
    timerError,
    startTimer,
    stopTimer,
    fetchRunningSession,
  };
}
