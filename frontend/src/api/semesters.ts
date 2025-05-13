import apiClient from "./apiClient";

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchSemester = async () => {
  const res = await apiClient.get(apiUrl + "/curriculum/current");
  return res.data;
};

export const fetchSemesters = async () => {
  const res = await apiClient.get(apiUrl + "/curriculum/semesters");
  return res.data;
};

export async function fetchSemestersByMajor(majorId: number) {
  const response = await apiClient.get(`/curriculum/majors/${majorId}/semesters`);
  return response.data;
}
