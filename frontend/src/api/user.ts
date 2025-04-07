import axios from 'axios'

export const fetchCurrentUser = async () => {
    const res = await axios.get("http://localhost:8080/api/users/me");
    return res.data;
};

