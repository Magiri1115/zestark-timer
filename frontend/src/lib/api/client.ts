import axios, { AxiosInstance, AxiosError, AxiosResponse } from 'axios';
import type { ApiError } from '@/types';

/**
 * API client configuration.
 */
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Axios instance with base configuration.
 */
export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Response interceptor for error handling.
 */
apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError<ApiError>) => {
    const status = error.response?.status;

    // Don't log 404 errors (expected for "no running session" cases)
    if (status !== 404) {
      if (error.response?.data) {
        const apiError: ApiError = error.response.data;
        console.error('API Error:', apiError);
      } else {
        const genericError: ApiError = {
          timestamp: new Date().toISOString(),
          status: status || 500,
          error: 'Network Error',
          message: error.message || 'An unexpected error occurred',
        };
        console.error('Network Error:', genericError);
      }
    }

    if (error.response?.data) {
      return Promise.reject(error.response.data);
    }

    const genericError: ApiError = {
      timestamp: new Date().toISOString(),
      status: status || 500,
      error: 'Network Error',
      message: error.message || 'An unexpected error occurred',
    };

    return Promise.reject(genericError);
  }
);

/**
 * Handles API requests with automatic error handling.
 *
 * @param method the HTTP method
 * @param url the endpoint URL
 * @param data optional request body data
 * @param params optional query parameters
 * @returns the response data
 */
export async function apiRequest<T>(
  method: 'GET' | 'POST' | 'PUT' | 'DELETE',
  url: string,
  data?: unknown,
  params?: Record<string, string | number>
): Promise<T> {
  const response = await apiClient.request<T>({
    method,
    url,
    data,
    params,
  });

  return response.data;
}
