import { useQuery } from "@tanstack/react-query";
import apiClient from "@/api/apiClient";

export function useMajors() {
  return useQuery({
    queryKey: ["majors"],
    queryFn: () => apiClient.get("/api/curriculum/majors").then(res => res.data),
  });
}