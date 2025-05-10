

import { useParams } from "react-router-dom";
import { useCourses } from "@/hooks/useCourses";

type Course = {
  id: number;
  name: string;
};

export default function Course() {
  const { semesterId } = useParams();
  const { data: courses, isLoading } = useCourses(+semesterId!);

  if (isLoading) return <p>Loading...</p>;

  return (
    <ul>
      {courses.map((course: Course) => (
        <li key={course.id}>{course.name}</li>
      ))}
    </ul>
  );
}