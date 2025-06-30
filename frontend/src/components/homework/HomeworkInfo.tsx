import { useQuery } from "@tanstack/react-query";
import { fetchHomework, uploadHomework, createHomework } from "../../api/homework";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Upload } from "lucide-react";
import { useState } from "react";
import { useUserStore } from "@/stores/userStore";

export default function HomeworkInfo() {
  const {
    data: homework,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["homework"],
    queryFn: fetchHomework,
  });

  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const user = useUserStore((state) => state.user);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return;

    setUploading(true);
    try {
      await uploadHomework(file.name, file);
      const filePath = `homeworks/${file.name}`;
      
      // Note: This component doesn't have lessonId, so we'll need to get it from homework data
      // For now, using a placeholder lessonId of 1
      await createHomework(
        user?.id,
        1, // lessonId - this should come from the homework data
        homework?.title || "Homework Assignment", // title
        `Homework for ${homework?.title || "Assignment"}`, // description
        filePath,
        1 // credits
      );
      alert("✅ Upload successful!");
    } catch (err) {
      console.error("❌ Upload failed:", err);
      alert("❌ Upload failed.");
    } finally {
      setUploading(false);
    }
  };

  if (isLoading) return <div className="p-6">Loading...</div>;
  if (isError)
    return (
      <div className="p-6 text-red-500">Error fetching assignment info</div>
    );

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">User Info</h1>
      <div className="space-y-2 text-lg">
        <p>
          <strong>Subject:</strong> {homework.title}
        </p>
        <p>
          <strong>Task:</strong> {homework.content}
        </p>
        <div>
          <p>
            <strong>Upload homework:</strong>
          </p>
          <Input type="file" placeholder="Upload homework" onChange={handleFileChange}/>
          <Button onClick={handleUpload} disabled={!file || uploading} className="mt-2">
            <Upload /> {uploading ? "Uploading..." : "Upload"}
          </Button>
          {/* {uploadSuccess && (
            <p className="text-green-600 mt-2">✅ File uploaded successfully!</p>
          )}
          {uploadError && (
            <p className="text-red-600 mt-2">{uploadError}</p>
          )}  */}
        </div>
      </div>
    </div>
  );
}
