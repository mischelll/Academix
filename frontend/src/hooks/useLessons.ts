import { useQuery } from "@tanstack/react-query";
import apiClient from "@/api/apiClient";

export function useLessons(lessonId: number) {
  return useQuery({
    queryKey: ["lessons"],
    queryFn: () => apiClient.get(`/api/curriculum/majors/${lessonId}/lessons`).then(res => res.data),
  });
}