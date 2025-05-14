import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent
} from "@/components/ui/tabs";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { fetchMajors } from "@/api/majors";
import { fetchSemestersByMajor } from "@/api/semesters";
import { fetchCoursesBySemester } from "@/api/courses";
import { fetchLessonsByCourse } from "@/api/lessons";

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
};

type Teacher = {
  id: number;
  name: string;
};

export default function Curriculum() {
  const [selectedMajorId, setSelectedMajorId] = useState<number | null>(null);
  const [selectedSemesterId, setSelectedSemesterId] = useState<number | null>(null);
  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);
  const [activeTab, setActiveTab] = useState("major");

  const { data: majors = [] } = useQuery<Major[]>({
    queryKey: ["majors"],
    queryFn: fetchMajors
  });

  const { data: semesters = [] } = useQuery<Semester[]>({
    queryKey: ["semesters", selectedMajorId],
    queryFn: () => fetchSemestersByMajor(selectedMajorId!),
    enabled: !!selectedMajorId && activeTab === "semesters"
  });

  const { data: courses = [] } = useQuery<Course[]>({
    queryKey: ["courses", selectedSemesterId],
    queryFn: () => fetchCoursesBySemester(selectedSemesterId!),
    enabled: !!selectedSemesterId && activeTab === "courses"
  });

  const { data: lessons = [] } = useQuery<Lesson[]>({
    queryKey: ["lessons", selectedCourseId],
    queryFn: () => fetchLessonsByCourse(selectedCourseId!),
    enabled: !!selectedCourseId && activeTab === "lessons"
  });
  
  const { data: teacher } = useQuery<Teacher>({
    queryKey: ["teacher", selectedCourseId],
    queryFn: () => fetchAssignedTeacher(selectedCourseId!),
    enabled: !!selectedCourseId && activeTab === "lessons"
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
          <TabsTrigger value="semesters" disabled={!selectedMajorId}>Semesters</TabsTrigger>
          <TabsTrigger value="courses" disabled={!selectedSemesterId}>Courses</TabsTrigger>
          <TabsTrigger value="lessons" disabled={!selectedCourseId}>Lessons</TabsTrigger>
        </TabsList>

        <TabsContent value="major" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {majors.map((major) => (
              <Card key={major.id} className={`cursor-pointer ${selectedMajorId === major.id ? "ring-2 ring-primary" : ""}`} onClick={() => {
                setSelectedMajorId(major.id);
                setSelectedSemesterId(null); // reset semester when major changes
              }}>
                <CardHeader>
                  <CardTitle>{major.name}</CardTitle>
                  <CardDescription>{major.description || "No description"}</CardDescription>
                </CardHeader>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="semesters" className="mt-4">
          <div className="grid grid-cols-2 gap-4">
            {semesters.map((sem) => (
              <Card key={sem.id} className={`cursor-pointer ${selectedSemesterId === sem.id ? "ring-2 ring-primary" : ""}`} onClick={() => setSelectedSemesterId(sem.id)}>
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
              <Card key={course.id} className={`cursor-pointer ${selectedCourseId === course.id ? "ring-2 ring-primary" : ""}`} onClick={() => setSelectedCourseId(course.id)}>
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
              <Card key={lesson.id}>
                <CardHeader>
                  <CardTitle>{lesson.title}</CardTitle>
                </CardHeader>
              </Card>
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
