import { useQuery } from "@tanstack/react-query";
import { fetchHomework, uploadHomework, createHomework } from "../../api/homework";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Upload, FileText, Shield } from "lucide-react";
import { useState } from "react";
import { useUserStore } from "@/stores/userStore";
import { Badge } from "../ui/badge";

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

  // Check if user is a student
  const isStudent = user?.roles?.some(role => 
    role.name === 'ROLE_STUDENT'
  );

  // Check if user is a teacher or admin
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

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
      const filePath = `${file.name}`;
      
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
      <h1 className="text-2xl font-bold mb-4">Homework Assignment</h1>
      <div className="space-y-4">
        <div>
          <p className="text-sm font-medium text-gray-500">Subject</p>
          <p className="text-lg">{homework.title}</p>
        </div>
        <div>
          <p className="text-sm font-medium text-gray-500">Task</p>
          <p className="text-lg">{homework.content}</p>
        </div>

        {isStudent ? (
          // Student view - can upload homework
          <div>
            <p className="text-sm font-medium text-gray-500 mb-2">Upload homework</p>
            <div className="space-y-2">
              <Input 
                type="file" 
                placeholder="Upload homework" 
                onChange={handleFileChange}
                disabled={uploading}
              />
              <Button 
                onClick={handleUpload} 
                disabled={!file || uploading} 
                className="mt-2"
              >
                <Upload className="w-4 h-4 mr-2" />
                {uploading ? "Uploading..." : "Upload"}
              </Button>
            </div>
          </div>
        ) : isTeacher ? (
          // Teacher view - access denied
          <div className="border border-red-200 rounded-lg p-4 bg-red-50">
            <div className="flex items-center gap-2 mb-2">
              <Shield className="w-5 h-5 text-red-600" />
              <Badge variant="destructive">Access Restricted</Badge>
            </div>
            <p className="text-red-700">
              Teachers cannot upload homework. Use the Teacher Dashboard to review and grade student submissions.
            </p>
            <Button 
              variant="outline" 
              size="sm" 
              className="mt-2"
              onClick={() => window.location.href = '/teacher-dashboard'}
            >
              Go to Teacher Dashboard
            </Button>
          </div>
        ) : (
          // Default view for users without specific roles
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
            <div className="flex items-center gap-2 mb-2">
              <FileText className="w-5 h-5 text-gray-600" />
              <Badge variant="outline">No Role Assigned</Badge>
            </div>
            <p className="text-gray-700">
              Please contact your administrator to assign appropriate roles for homework access.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
