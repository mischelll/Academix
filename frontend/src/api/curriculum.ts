import apiClient from "./apiClient";

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchCurriculum = async () => {
  const res = await apiClient.get(apiUrl + "/curriculum");

  if (res.status !== 200) {
    throw new Error("Homework creation is unsuccessful!");
  }

  return res.data;
};