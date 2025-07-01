import { Tabs, TabsList, TabsTrigger, TabsContent } from '../components/ui/tabs';
import { useQuery } from '@tanstack/react-query';
import { fetchUserHomeworks, Homework, HomeworkStatus, fetchTeacherHomeworks } from '../api/homework';
import { useUserStore } from '../stores/userStore';
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "./ui/table";
import { Card, CardHeader, CardTitle, CardContent } from "./ui/card";
import { Badge } from "./ui/badge";
import { Check, Clock, X, FileText } from "lucide-react";
import { fetchCurriculum } from '../api/curriculum';
import { fetchLessonsByCourse } from '../api/lessons';
import CountdownTimer from './curriculum/utils/CountdownTimer';

function StatusBadge({ status }: { status: HomeworkStatus }) {
  let color = "bg-gray-100 text-gray-700";
  let icon = <FileText className="w-4 h-4 mr-1" />;
  let label = status;
  switch (status) {
    case HomeworkStatus.REVIEWED:
      color = "bg-green-100 text-green-700";
      icon = <Check className="w-4 h-4 mr-1" />;
      label = "Reviewed";
      break;
    case HomeworkStatus.SUBMITTED:
      color = "bg-blue-100 text-blue-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Pending Review";
      break;
    case HomeworkStatus.REVIEWING:
      color = "bg-yellow-100 text-yellow-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Under Review";
      break;
    case HomeworkStatus.RETURNED:
      color = "bg-red-100 text-red-700";
      icon = <X className="w-4 h-4 mr-1" />;
      label = "Returned";
      break;
    case HomeworkStatus.OPEN:
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

export default function Home() {
  const user = useUserStore((state) => state.user);
  const isTeacher = user?.roles?.some(role => role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN');
  const isStudent = user?.roles?.some(role => role.name === 'ROLE_STUDENT');

  // For teachers: fetch their homeworks (which includes lesson/course info)
  const { data: teacherHomeworks, isLoading: isTeacherLoading, error: teacherError } = useQuery({
    queryKey: ["teacherHomeworks"],
    queryFn: fetchTeacherHomeworks,
    enabled: isTeacher,
  });

  // For students: fetch their homeworks
  const { data: homeworks, isLoading, error } = useQuery({
    queryKey: ["userHomeworks", user?.id],
    queryFn: () => fetchUserHomeworks(user!.id),
    enabled: !!user?.id && !isTeacher,
  });

  // For students: fetch all lessons for their courses (via curriculum)
  const { data: curriculum, isLoading: isCurriculumLoading } = useQuery({
    queryKey: ["studentCurriculum", user?.id],
    queryFn: fetchCurriculum,
    enabled: !!user?.id && isStudent,
  });

  const homeworkList: Homework[] = homeworks?.content || homeworks || [];

  const submitted = homeworkList.filter(
    (hw) =>
      hw.status === HomeworkStatus.SUBMITTED ||
      hw.status === HomeworkStatus.REVIEWING ||
      hw.status === HomeworkStatus.REVIEWED ||
      hw.status === HomeworkStatus.RETURNED
  );

  // For unsubmitted: find all lessons the student is enrolled in, and filter those with no submitted homework
  let unsubmittedLessons: any[] = [];
  if (isStudent && curriculum && curriculum.courses) {
    // Flatten all lessons from all courses
    const allLessons = curriculum.courses.flatMap((course: any) => course.lessons || []);
    // Find lessons with no submitted homework
    unsubmittedLessons = allLessons.filter((lesson: any) => {
      return !homeworkList.some(hw => hw.lessonId === lesson.id && hw.status !== HomeworkStatus.OPEN);
    });
  }

  if (isTeacher) {
    // Show teacher's courses/lessons
    return (
      <Card className="w-full max-w-3xl mx-auto mt-10 shadow-lg border">
        <CardHeader>
          <CardTitle className="text-2xl font-bold">My Teaching Overview</CardTitle>
        </CardHeader>
        <CardContent>
          {isTeacherLoading ? (
            <div className="p-6">Loading...</div>
          ) : teacherError ? (
            <div className="p-6 text-red-500">Error loading your lessons/courses</div>
          ) : !teacherHomeworks || teacherHomeworks.length === 0 ? (
            <EmptyState text="You are not assigned to any courses or lessons yet." />
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Lesson</TableHead>
                  <TableHead>Student</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Deadline</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {teacherHomeworks.map(hw => (
                  <TableRow key={hw.id} className="hover:bg-muted/50 transition">
                    <TableCell className="font-medium text-base">{hw.lessonTitle}</TableCell>
                    <TableCell>{hw.studentName}</TableCell>
                    <TableCell><StatusBadge status={hw.status as HomeworkStatus} /></TableCell>
                    <TableCell>{hw.deadline ? new Date(hw.deadline).toLocaleDateString() : '-'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="w-full max-w-3xl mx-auto mt-10 shadow-lg border">
      <CardHeader>
        <CardTitle className="text-2xl font-bold">My Assignments Overview</CardTitle>
      </CardHeader>
      <CardContent>
        <Tabs defaultValue="submitted" className="w-full">
          <TabsList className="mb-4">
            <TabsTrigger value="submitted">Submitted</TabsTrigger>
            <TabsTrigger value="unsubmitted">Unsubmitted</TabsTrigger>
          </TabsList>
          <TabsContent value="submitted">
            {isLoading ? (
              <div className="p-6">Loading...</div>
            ) : error ? (
              <div className="p-6 text-red-500">Error loading assignments</div>
            ) : submitted.length === 0 ? (
              <EmptyState text="No submitted assignments." />
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Deadline</TableHead>
                    <TableHead>Grade</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {submitted.map((hw) => (
                    <TableRow key={hw.id} className="hover:bg-muted/50 transition">
                      <TableCell className="font-medium text-base">{hw.title}</TableCell>
                      <TableCell><StatusBadge status={hw.status} /></TableCell>
                      <TableCell>{hw.deadline ? new Date(hw.deadline).toLocaleDateString() : '-'}</TableCell>
                      <TableCell>
                        {hw.grade ? (
                          <span className="font-semibold text-green-700">{hw.grade.toFixed(2)}</span>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </TabsContent>
          <TabsContent value="unsubmitted">
            {isLoading || isCurriculumLoading ? (
              <div className="p-6">Loading...</div>
            ) : error ? (
              <div className="p-6 text-red-500">Error loading assignments</div>
            ) : !isStudent ? (
              <EmptyState text="No unsubmitted assignments." />
            ) : unsubmittedLessons.length === 0 ? (
              <EmptyState text="No unsubmitted assignments." />
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Lesson</TableHead>
                    <TableHead>Course</TableHead>
                    <TableHead>Deadline</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {unsubmittedLessons.map((lesson) => (
                    <TableRow key={lesson.id} className="hover:bg-muted/50 transition">
                      <TableCell className="font-medium text-base">{lesson.title}</TableCell>
                      <TableCell>{lesson.courseName || '-'}</TableCell>
                      <TableCell>
                        {lesson.endTimeMs && lesson.endTimeMs > Date.now() ? (
                          <CountdownTimer duration={lesson.endTimeMs - Date.now()} />
                        ) : (
                          <span className="text-red-500">Expired</span>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </TabsContent>
        </Tabs>
      </CardContent>
    </Card>
  );
}
