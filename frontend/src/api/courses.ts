import apiClient from "./apiClient";

export async function fetchCoursesBySemester(semesterId: number) {
  const response = await apiClient.get(
    `/curriculum/semesters/${semesterId}/courses`
  );
  return response.data;
}


export async function fetchAssignedTeacher(courseId: number) {
    const response = await apiClient.get(
        `/curriculum/course-teachers/course/${courseId}`
      );
      return response.data;
}