/**
 * Formats duration in seconds to HH:MM:SS format.
 *
 * @param durationInSeconds the duration in seconds
 * @returns formatted duration string (HH:MM:SS)
 */
export function formatDuration(durationInSeconds: number): string {
  const hours = Math.floor(durationInSeconds / 3600);
  const minutes = Math.floor((durationInSeconds % 3600) / 60);
  const seconds = Math.floor(durationInSeconds % 60);

  const paddedHours = String(hours).padStart(2, '0');
  const paddedMinutes = String(minutes).padStart(2, '0');
  const paddedSeconds = String(seconds).padStart(2, '0');

  return `${paddedHours}:${paddedMinutes}:${paddedSeconds}`;
}

/**
 * Calculates elapsed time in seconds between two timestamps.
 *
 * @param startTimeMs the start time in milliseconds
 * @param endTimeMs the end time in milliseconds (default: current time)
 * @returns elapsed time in seconds
 */
export function calculateElapsedTimeInSeconds(
  startTimeMs: number,
  endTimeMs: number = Date.now()
): number {
  return Math.floor((endTimeMs - startTimeMs) / 1000);
}

/**
 * Formats an ISO date string to a user-friendly format.
 *
 * @param isoDateString the ISO date string
 * @returns formatted date string (YYYY/MM/DD HH:MM)
 */
export function formatDateTime(isoDateString: string): string {
  // Parse ISO string as UTC by appending 'Z' if not present
  const dateString = isoDateString.endsWith('Z')
    ? isoDateString
    : isoDateString + 'Z';
  const dateObject = new Date(dateString);
  const year = dateObject.getFullYear();
  const month = String(dateObject.getMonth() + 1).padStart(2, '0');
  const day = String(dateObject.getDate()).padStart(2, '0');
  const hours = String(dateObject.getHours()).padStart(2, '0');
  const minutes = String(dateObject.getMinutes()).padStart(2, '0');

  return `${year}/${month}/${day} ${hours}:${minutes}`;
}
