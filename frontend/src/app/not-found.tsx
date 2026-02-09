import Link from 'next/link';

/**
 * 404 Not Found page component.
 */
export default function NotFound() {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <h2 className="text-4xl font-bold mb-4">404</h2>
        <p className="text-gray-600 mb-4">ページが見つかりませんでした</p>
        <Link
          href="/"
          className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600 inline-block"
        >
          ホームに戻る
        </Link>
      </div>
    </div>
  );
}
