import axios from 'axios';
import { redirect } from './navigation';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL
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
    (err) => {
        const status = err.response?.status;

        if (status === 401 || status === 403) {
          console.warn("🔐 Unauthorized. Redirecting to login...");
          localStorage.removeItem("token");
          redirect("/login");
          return;
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