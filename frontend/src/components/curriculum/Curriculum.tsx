import { useEffect, useState } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { fetchMajors } from "@/api/majors";
import { fetchSemestersByMajor } from "@/api/semesters";
import { fetchAssignedTeacher, fetchCoursesBySemester } from "@/api/courses";
import { fetchLessonsByCourse } from "@/api/lessons";
import CountdownTimer from "./utils/CountdownTimer";
import { Upload } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { uploadHomework, createHomework } from "@/api/homework";
import { User, useUserStore } from "@/stores/userStore";
import { toast } from "sonner";

type Major = {
  id: number;
  name: string;
  description?: string;
};

type Semester = {
  id: number;
  name: string;
  majorId: number;
};

type Course = {
  id: number;
  name: string;
  description?: string;
  semesterId: number;
};

type Lesson = {
  id: number;
  title: string;
  courseId: number;
  endTimeMs: number;
};

type Teacher = {
  id: number;
  name: string;
};

function LessonCard({ lesson, user }: { lesson: Lesson; user: User | null}) {
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const queryClient = useQueryClient();
  
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file || !user) return;
    setUploading(true);
    
    try {
      // Upload file to S3
      await uploadHomework(file.name, file);
      
      // Create homework record in database
      const filePath = `homeworks/${file.name}`;
      await createHomework(
        user.id,
        lesson.id, // lessonId
        lesson.title, // title
        `Homework for ${lesson.title}`, // description
        filePath,
        1 // credits
      );
      
      // Invalidate user homeworks query to refresh assignments list
      queryClient.invalidateQueries({ queryKey: ["userHomeworks", user.id] });
      
      toast.success("Homework uploaded successfully! Check your assignments page.");
      
      // Clear the file input
      setFile(null);
      const fileInput = document.getElementById(lesson.id + "") as HTMLInputElement;
      if (fileInput) fileInput.value = "";
      
    } catch (err) {
      console.error("❌ Upload failed:", err);
      toast.error("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
  };

  // const handleDownload = async () => {
  //   setDownloading(true);
  //   try {
  //     const res = await apiClient.get(`/homeworks/${lesson.id}/download-url`);
  //     const presignedUrl = res.data;
  //     window.open(presignedUrl, "_blank");
  //   } catch (err) {
  //     console.error("❌ Download failed:", err);
  //     alert("❌ Download failed.");
  //   } finally {
  //     setDownloading(false);
  //   }
  // };

  return (
    <Card key={lesson.id}>
      <CardHeader>
        <CardTitle>{lesson.title}</CardTitle>
      </CardHeader>
      <CardContent>
        <CountdownTimer
          duration={lesson.endTimeMs - Date.now()}
          onExpire={() =>
            console.log(`Timer expired for lesson: ${lesson.title}`)
          }
        />
        <Input
          id={lesson.id + ""}
          type="file"
          placeholder="Upload homework"
          onChange={handleFileChange}
        />
        <Button
          onClick={handleUpload}
          disabled={!file || uploading}
          className="mt-2"
        >
          <Upload /> {uploading ? "Uploading..." : "Upload"}
        </Button>
        {/* <Button
          onClick={handleDownload}
          disabled={downloading || !file}
          className="mt-2"
        >
          <Download className="mr-2 h-4 w-4" />
          {downloading ? "Downloading..." : "Download"}
        </Button> */}
      </CardContent>
    </Card>
  );
}

export default function Curriculum() {
  const [selectedMajorId, setSelectedMajorId] = useState<number | null>(null);
  const [selectedSemesterId, setSelectedSemesterId] = useState<number | null>(
    null
  );
  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);
  const [activeTab, setActiveTab] = useState("major");
  const user = useUserStore((state) => state.user);

  const { data: majors = [] } = useQuery<Major[]>({
    queryKey: ["majors"],
    queryFn: fetchMajors,
  });

  const { data: semesters = [] } = useQuery<Semester[]>({
    queryKey: ["semesters", selectedMajorId],
    queryFn: () => fetchSemestersByMajor(selectedMajorId!),
    enabled: !!selectedMajorId && activeTab === "semesters",
  });

  const { data: courses = [] } = useQuery<Course[]>({
    queryKey: ["courses", selectedSemesterId],
    queryFn: () => fetchCoursesBySemester(selectedSemesterId!),
    enabled: !!selectedSemesterId && activeTab === "courses",
  });

  const { data: lessons = [] } = useQuery<Lesson[]>({
    queryKey: ["lessons", selectedCourseId],
    queryFn: () => fetchLessonsByCourse(selectedCourseId!),
    enabled: !!selectedCourseId && activeTab === "lessons",
  });

  const { data: teacher } = useQuery<Teacher>({
    queryKey: ["teacher", selectedCourseId],
    queryFn: () => fetchAssignedTeacher(selectedCourseId!),
    enabled: !!selectedCourseId && activeTab === "lessons",
  });

  useEffect(() => {
    if (majors.length > 0 && selectedMajorId === null) {
      setSelectedMajorId(majors[0].id);
      setSelectedSemesterId(null); // reset semester initially
    }
  }, [majors, selectedMajorId]);


  return (
    <>
      <Tabs
        defaultValue="major"
        className="w-full"
        onValueChange={(tab) => setActiveTab(tab)}
      >
        <TabsList className="flex gap-4">
          <TabsTrigger value="major">Majors</TabsTrigger>
          <TabsTrigger value="semesters" disabled={!selectedMajorId}>
            Semesters
          </TabsTrigger>
          <TabsTrigger value="courses" disabled={!selectedSemesterId}>
            Courses
          </TabsTrigger>
          <TabsTrigger value="lessons" disabled={!selectedCourseId}>
            Lessons
          </TabsTrigger>
        </TabsList>

        <TabsContent value="major" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {majors.map((major) => (
              <Card
                key={major.id}
                className={`cursor-pointer ${
                  selectedMajorId === major.id ? "ring-2 ring-primary" : ""
                }`}
                onClick={() => {
                  setSelectedMajorId(major.id);
                  setSelectedSemesterId(null); // reset semester when major changes
                }}
              >
                <CardHeader>
                  <CardTitle>{major.name}</CardTitle>
                  <CardDescription>
                    {major.description || "No description"}
                  </CardDescription>
                </CardHeader>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="semesters" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {semesters.map((sem) => (
              <Card
                key={sem.id}
                className={`cursor-pointer ${
                  selectedSemesterId === sem.id ? "ring-2 ring-primary" : ""
                }`}
                onClick={() => setSelectedSemesterId(sem.id)}
              >
                <CardHeader>
                  <CardTitle>{sem.name}</CardTitle>
                </CardHeader>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="courses" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {courses.map((course) => (
              <Card
                key={course.id}
                className={`cursor-pointer ${
                  selectedCourseId === course.id ? "ring-2 ring-primary" : ""
                }`}
                onClick={() => setSelectedCourseId(course.id)}
              >
                <CardHeader>
                  <CardTitle>{course.name}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p>{course.description || "No info"}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="lessons" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {lessons.map((lesson) => (
              <LessonCard key={lesson.id} lesson={lesson} user={user} />
            ))}
            {teacher && (
              <Card className="col-span-2">
                <CardHeader>
                  <CardTitle>Assigned Teacher</CardTitle>
                  <CardDescription>{teacher.name}</CardDescription>
                </CardHeader>
              </Card>
            )}
          </div>
        </TabsContent>
      </Tabs>
    </>
  );
}
