import apiClient from "./apiClient";

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchCurriculum = async () => {
  const res = await apiClient.get(apiUrl + "/curriculum");

  if (res.status !== 200) {
    throw new Error("Homework creation is unsuccessful!");
  }

  return res.data;
};

export const backfillHomeworksForStudent = async (studentId: number) => {
  await apiClient.post(apiUrl + `/curriculum/homeworks/backfill-for-student/${studentId}`);
};