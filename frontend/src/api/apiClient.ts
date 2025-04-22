import axios from 'axios';
import { redirect } from './navigation';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL,
    withCredentials: true
});

const refreshClient = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL,
  withCredentials: true
});

apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('authToken');

    if (token) {
        config.headers.Authorization = 'Bearer ' + token;
    }
    return config;
});

apiClient.interceptors.response.use(
    (res) => res,
     async (err) => {
        const originalRequest = err.config;
        const status = err.response?.status;
        const accessToken = localStorage.getItem("authToken");

        if (err.response?.status === 401 && accessToken && !originalRequest._retry ) {
          originalRequest._retry = true;
    
          try {
            const refreshRes = await refreshClient.post("/auth/refresh");
            const newAccessToken = refreshRes.data.accessToken;
    
            localStorage.setItem("authToken", newAccessToken);
            originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
    
            // Retry original request
            return apiClient(originalRequest);
          } catch (refreshError) {
            console.warn("🔐 Refresh failed, logging out...", refreshError);
            localStorage.removeItem("authToken");
            window.location.href = "/login";
            return Promise.reject(refreshError);
          }
        }

        if (status === 401 || status === 403) {
          console.warn("🔐 Unauthorized. Redirecting to login...");
          localStorage.removeItem("authToken");
          redirect("/login");
          return Promise.reject(err);
        }
    
        if (!status) {
          console.error("❌ Network or CORS error (no response):", err);
          redirect("/login");
          return;
        }
    
        console.error("❌ API error:", status, err.response?.data);
        return Promise.reject(err); 
    }
  );

export default apiClient;