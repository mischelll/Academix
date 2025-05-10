import { useSemesters } from "@/hooks/useSemesters";
import { Link, useParams } from "react-router-dom";

export type Semester = {
  id: number;
  name: string;
};

function Semester() {
  const { majorId } = useParams();
  const { data: semesters, isLoading, isError } = useSemesters(+majorId!);

  if (isLoading) return <p>Loading...</p>;
  if (isError) return <p>Something went wrong!</p>;

  return (
    <>
    This is Major NAME. Blalala
    Semesters are below
      <ul>
        {semesters?.map((semester: Semester) => (
          <li key={semester.id}>
            <Link
              to={`/curriculum/semesters/${semester.id}/courses`}
              className="text-blue-600 hover:underline"
            >
              {semester.name}
            </Link>
          </li>
        ))}
      </ul>
    </>
  );
}

export default Semester;
