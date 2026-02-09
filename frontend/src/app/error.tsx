'use client';

/**
 * Error boundary component for handling errors in the app.
 */
export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <h2 className="text-2xl font-bold mb-4">エラーが発生しました</h2>
        <p className="text-gray-600 mb-4">{error.message}</p>
        <button
          onClick={() => reset()}
          className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600"
        >
          再試行
        </button>
      </div>
    </div>
  );
}
