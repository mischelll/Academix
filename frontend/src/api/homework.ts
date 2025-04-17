import apiClient from './apiClient';

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchHomework = async () => {
    const res = await apiClient.get(apiUrl + "/homeworks/current");
    return res.data;
};
