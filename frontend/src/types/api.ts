/**
 * Generic API response wrapper.
 */
export interface ApiResponse<T> {
  data?: T;
  error?: ApiError;
}

/**
 * API error structure matching backend GlobalExceptionHandler.
 */
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  errors?: Record<string, string>;
}

/**
 * HTTP method types.
 */
export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

/**
 * API request config.
 */
export interface ApiRequestConfig {
  method: HttpMethod;
  url: string;
  data?: unknown;
  params?: Record<string, string | number>;
}
