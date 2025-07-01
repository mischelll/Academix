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
import { Upload, Loader2, Calendar, FileText, Check, Clock, X } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { uploadHomework, createHomework } from "@/api/homework";
import { User, useUserStore } from "@/stores/userStore";
import { toast } from "sonner";
import { Badge } from "../ui/badge";
import { format } from "date-fns";

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

function StatusBadge({ status }: { status: string }) {
  // This is a mock; in a real app, fetch the user's homework for this lesson
  let color = "bg-gray-100 text-gray-700";
  let icon = <FileText className="w-4 h-4 mr-1" />;
  let label = status;
  switch (status) {
    case "REVIEWED":
      color = "bg-green-100 text-green-700";
      icon = <Check className="w-4 h-4 mr-1" />;
      label = "Reviewed";
      break;
    case "SUBMITTED":
      color = "bg-blue-100 text-blue-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Pending Review";
      break;
    case "REVIEWING":
      color = "bg-yellow-100 text-yellow-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Under Review";
      break;
    case "RETURNED":
      color = "bg-red-100 text-red-700";
      icon = <X className="w-4 h-4 mr-1" />;
      label = "Returned";
      break;
    case "OPEN":
      color = "bg-gray-100 text-gray-700";
      icon = <FileText className="w-4 h-4 mr-1" />;
      label = "Open";
      break;
  }
  return (
    <span className={`inline-flex items-center rounded px-2 py-1 text-xs font-medium ${color}`}>
      {icon} {label}
    </span>
  );
}

function EmptyState({ text }: { text: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-12 text-gray-400">
      <FileText className="w-10 h-10 mb-2" />
      <div className="text-lg font-medium">{text}</div>
    </div>
  );
}

function LessonCard({ lesson, user }: { lesson: Lesson; user: User | null}) {
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const queryClient = useQueryClient();
  const [fileName, setFileName] = useState<string | null>(null);

  // Check if user is a student (not teacher or admin)
  const isStudent = user?.roles?.some(role => 
    role.name === 'ROLE_STUDENT'
  );

  // Check if user is a teacher or admin
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

  // Mock status for now; in a real app, fetch the user's homework for this lesson
  const status = "OPEN";

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      setFile(e.target.files[0]);
      setFileName(e.target.files[0].name);
    }
  };

  const handleUpload = async () => {
    if (!file || !user) return;
    setUploading(true);
    try {
      await uploadHomework(file.name, file);
      const filePath = `${file.name}`;
      await createHomework(
        user.id,
        lesson.id,
        lesson.title,
        `Homework for ${lesson.title}`,
        filePath,
        1
      );
      queryClient.invalidateQueries({ queryKey: ["userHomeworks", user.id] });
      toast.success("Homework uploaded successfully! Check your assignments page.");
      setFile(null);
      setFileName(null);
      const fileInput = document.getElementById(lesson.id + "") as HTMLInputElement;
      if (fileInput) fileInput.value = "";
    } catch (err) {
      console.error("❌ Upload failed:", err);
      toast.error("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
  };

  return (
    <Card className="relative border shadow-md hover:shadow-lg transition group">
      <CardHeader className="pb-2">
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-semibold">{lesson.title}</CardTitle>
          <StatusBadge status={status} />
        </div>
        <div className="flex items-center text-sm text-gray-500 mt-1 gap-2">
          <Calendar className="w-4 h-4 mr-1" />
          {format(new Date(lesson.endTimeMs), "MMM dd, yyyy")}
          <span className="ml-2">
            {lesson.endTimeMs && lesson.endTimeMs > Date.now() ? (
              <CountdownTimer duration={lesson.endTimeMs - Date.now()} />
            ) : (
              <span className="text-red-500">Expired</span>
            )}
          </span>
        </div>
      </CardHeader>
      <CardContent className="pt-0">
        {isStudent ? (
          // Student view - can upload homework
          <div className="flex flex-col gap-2">
            <div className="flex items-center gap-2">
              <Input
                id={lesson.id + ""}
                type="file"
                placeholder="Upload homework"
                onChange={handleFileChange}
                className="w-auto"
                disabled={uploading}
              />
              {fileName && <span className="text-xs text-gray-500 truncate max-w-[120px]">{fileName}</span>}
            </div>
            <Button
              onClick={handleUpload}
              disabled={!file || uploading}
              className="mt-1 w-fit"
            >
              {uploading ? <Loader2 className="w-4 h-4 mr-2 animate-spin" /> : <Upload className="w-4 h-4 mr-2" />}
              {uploading ? "Uploading..." : "Upload"}
            </Button>
          </div>
        ) : isTeacher ? (
          // Teacher view - can only view lesson info
          <div className="flex flex-col gap-2">
            <div className="flex items-center gap-2 text-sm text-gray-600">
              <FileText className="w-4 h-4" />
              <span>Lesson content and materials</span>
            </div>
            <div className="text-sm text-gray-500">
              As a teacher, you can view and manage submitted homeworks in the Teacher Dashboard.
            </div>
            <Button
              variant="outline"
              size="sm"
              className="mt-1 w-fit"
              onClick={() => window.location.href = '/teacher-dashboard'}
            >
              View Teacher Dashboard
            </Button>
          </div>
        ) : (
          // Default view for users without specific roles
          <div className="text-sm text-gray-500">
            Please contact your administrator to assign appropriate roles.
          </div>
        )}
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

  // Check user roles
  const isStudent = user?.roles?.some(role => 
    role.name === 'ROLE_STUDENT'
  );
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

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
      {/* Role indicator */}
      <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-lg font-semibold text-blue-900">Curriculum Access</h2>
            <p className="text-sm text-blue-700">
              {isStudent && "You can view lessons and upload homework assignments."}
              {isTeacher && "You can view lessons and manage homework submissions in the Teacher Dashboard."}
              {!isStudent && !isTeacher && "Please contact your administrator to assign appropriate roles."}
            </p>
          </div>
          <div className="flex gap-2">
            {isStudent && (
              <Badge variant="default" className="bg-green-100 text-green-800">
                Student Access
              </Badge>
            )}
            {isTeacher && (
              <Badge variant="default" className="bg-purple-100 text-purple-800">
                Teacher Access
              </Badge>
            )}
            {!isStudent && !isTeacher && (
              <Badge variant="outline" className="text-gray-600">
                No Role Assigned
              </Badge>
            )}
          </div>
        </div>
      </div>

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
          {lessons.length === 0 ? (
            <EmptyState text="No lessons found." />
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
          )}
        </TabsContent>
      </Tabs>
    </>
  );
}
