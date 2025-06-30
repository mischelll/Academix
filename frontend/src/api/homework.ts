import apiClient from "./apiClient";

// Use API Gateway URL (port 8080)
const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL?.replace(':8081', ':8080') || "http://localhost:8080/api";

// Types based on backend Homework entity
export interface Homework {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  deadline: string;
  credits: number;
  studentId: number;
  lessonId: number;
  submittedDate?: string;
  grade?: number;
  comment?: string;
  filePath?: string;
  status: HomeworkStatus;
}

export enum HomeworkStatus {
  OPEN = "OPEN",
  SUBMITTED = "SUBMITTED", 
  REVIEWING = "REVIEWING",
  REVIEWED = "REVIEWED",
  RETURNED = "RETURNED"
}

export const fetchHomework = async () => {
  const res = await apiClient.get(apiUrl + "/homeworks/current");
  return res.data;
};

// Fetch all homeworks for the current user
export const fetchUserHomeworks = async (studentId: number) => {
  const res = await apiClient.get(`${apiUrl}/homeworks/student/${studentId}`);
  return res.data;
};

export const createHomework = async (
  studentId: number | undefined, 
  lessonId: number,
  title: string,
  description: string,
  filePath: string,
  credits: number = 1
) => {
  const res = await apiClient.post(apiUrl + "/homeworks", {
    studentId: studentId,
    lessonId: lessonId,
    title: title,
    description: description,
    filePath: filePath,
    credits: credits,
    content: description // Backend expects this field
  });

  if (res.status !== 201) {
    throw new Error("Homework creation is unsuccessful!");
  }

  return res.data;
};

export const uploadHomework = async (fileName: string, file: File): Promise<void> => {
  const getPresignedUrlRes = await apiClient.get(
    apiUrl + "/files/presigned-url",
    {
      params: { fileName },
    }
  );
  const preSignedUrl = getPresignedUrlRes.data;

  const uploadRes = await fetch(preSignedUrl, {
    method: "PUT",
    headers: {
      "Content-Type": "application/octet-stream",
    },
    body: file,
  });

  if (!uploadRes.ok) {
    throw new Error("Upload to S3 failed");
  }
};

// Download homework file using lessonId
export const downloadHomework = async (lessonId: number) => {
  const res = await apiClient.get(`${apiUrl}/homeworks/${lessonId}/download-url`);
  const downloadUrl = res.data;
  
  // Create a temporary link to download the file
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', 'homework.pdf');
  document.body.appendChild(link);
  link.click();
  link.remove();
};
