import apiClient from "./apiClient";

export async function fetchCoursesBySemester(semesterId: number) {
    const response = await apiClient.get(`/curriculum/semesters/${semesterId}/courses`);
    return response.data;
  }