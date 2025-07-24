// API Configuration utility for Spring Boot backend communication

/**
 * Get the API base URL for Spring Boot backend
 * Spring Boot runs on port 5000 in Replit environment
 * Can be overridden with VITE_API_BASE_URL environment variable
 */
export const getApiBaseUrl = (): string => {
  // Check if we should use external backend
  if (import.meta.env.VITE_API_BASE_URL) {
    return import.meta.env.VITE_API_BASE_URL;
  }
  
  // In development, connect to Spring Boot backend on same port
  // Spring Boot serves both API and static React frontend
  if (import.meta.env.MODE === 'development') {
    return ''; // Use relative URLs since Spring Boot serves everything
  }
  
  // In production, Spring Boot serves the React frontend as static content
  return '';
};

/**
 * Build complete API URL by combining base URL with endpoint
 * @param endpoint - API endpoint (e.g., '/api/devotees')
 * @returns Full API URL
 */
export const buildApiUrl = (endpoint: string): string => {
  const baseUrl = getApiBaseUrl();
  // Remove trailing slash from base URL and leading slash from endpoint if both exist
  const cleanBaseUrl = baseUrl.replace(/\/$/, '');
  const cleanEndpoint = endpoint.startsWith('/') ? endpoint : `/${endpoint}`;
  
  return `${cleanBaseUrl}${cleanEndpoint}`;
};

/**
 * API configuration object
 */
export const API_CONFIG = {
  baseUrl: getApiBaseUrl(),
  timeout: 10000, // 10 seconds
  defaultHeaders: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
};

/**
 * Check if we're running in development mode
 */
export const isDevelopment = (): boolean => {
  return import.meta.env.MODE === 'development';
};

/**
 * Log API configuration (useful for debugging)
 */
export const logApiConfig = (): void => {
  if (isDevelopment()) {
    console.log('🚀 API Configuration:', {
      baseUrl: getApiBaseUrl(),
      mode: import.meta.env.MODE,
      isDev: isDevelopment(),
    });
  }
};