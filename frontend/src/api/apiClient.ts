import axios from 'axios';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL
});

apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('authToken');
    if(token) {
        config.headers.Authorization = 'Bearer ' + token;
    }
    return config;
});

export default apiClient;