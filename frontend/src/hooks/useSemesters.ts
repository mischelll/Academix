import { useQuery } from "@tanstack/react-query";
import apiClient from "@/api/apiClient";

export function useSemesters(majorId: number) {
  return useQuery({
    queryKey: ["semesters", majorId],
    queryFn: () =>
      apiClient.get(`/api/curriculum/majors/${majorId}/semesters`).then(res => res.data),
    enabled: !!majorId,
  });
}