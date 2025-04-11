import axios from 'axios';
import apiClient from './apiClient';

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchCurrentUser = async () => {
    const res = await axios.get(apiUrl + "/users/me");
    return res.data;
};

export const fetchProtectedCurrentUser = async () => {
    const res = await apiClient.get(apiUrl + "/users/protected/me");
    return res.data;
};

