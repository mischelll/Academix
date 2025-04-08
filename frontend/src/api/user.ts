import axios from 'axios'

const apiUrl = import.meta.env.VITE_BACKEND_USER_SERVICE_URL;

export const fetchCurrentUser = async () => {
    const res = await axios.get(apiUrl + "/api/users/me");
    return res.data;
};

