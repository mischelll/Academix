import { useQuery } from "@tanstack/react-query";
import apiClient from "@/api/apiClient";

export function useCourses(semesterId: number) {
  return useQuery({
    queryKey: ["courses", semesterId],
    queryFn: () =>
      apiClient.get(`/api/curriculum/semesters/${semesterId}/courses`).then(res => res.data),
    enabled: !!semesterId,
  });
}