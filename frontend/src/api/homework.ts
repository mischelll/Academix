import apiClient from "./apiClient";

const apiUrl = import.meta.env.VITE_BACKEND_LOCAL_USER_SERVICE_URL;

export const fetchHomework = async () => {
  const res = await apiClient.get(apiUrl + "/homeworks/current");
  return res.data;
};

export const createHomework = async (studentId: number | undefined, filePath: string | undefined) => {
  const res = await apiClient.post(apiUrl + "/homeworks", {
    studentId: studentId,
    filePath: filePath,
  });

  if (res.status !== 201) {
    throw new Error("Homework creation is unsuccessful!");
  }

  return res.data;
};

export const uploadHomework = async (fileName: string, file: File) => {
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

  return uploadRes.url;
};
