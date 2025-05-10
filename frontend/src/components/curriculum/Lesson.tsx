import { useParams } from "react-router-dom";
import { useLessons } from "@/hooks/useLessons";

type Lesson = {
  id: number;
  title: string;
};

export default function Lesson() {
  const { courseId } = useParams<{ courseId: string }>();
  const { data: lessons, isLoading, isError } = useLessons(+courseId!);

  if (isLoading) return <div>Loading lessons...</div>;
  if (isError) return <div>Error loading lessons</div>;

  return (
    <div className="p-6 space-y-2">
      <h2 className="text-2xl font-bold">Lessons for Course {courseId}</h2>
      <ul className="list-disc pl-6">
        {lessons?.map((lesson: Lesson) => (
          <li key={lesson.id}>{lesson.title}</li>
        ))}
      </ul>
    </div>
  );
}
