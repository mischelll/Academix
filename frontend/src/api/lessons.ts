import apiClient from "./apiClient";

export async function fetchLessonsByCourse(courseId: number) {
  const response = await apiClient.get(`/curriculum/courses/${courseId}/lessons`);
  return response.data;
}