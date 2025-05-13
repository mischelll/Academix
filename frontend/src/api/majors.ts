import apiClient from "./apiClient";

export async function fetchMajors() {
  const response = await apiClient.get("/curriculum/majors");
  return response.data;
}