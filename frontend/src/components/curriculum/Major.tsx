import { Link } from "react-router-dom";

export default function MajorList() {
  const majors = [
    { id: 1, name: "Software Engineering" },
    { id: 2, name: "AI" },
  ];

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">Majors</h2>
      <ul className="space-y-2">
        {majors.map((major) => (
          <li key={major.id}>
            <Link
              to={`/curriculum/majors/${major.id}/semesters`}
              className="text-blue-600 hover:underline"
            >
              {major.name}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
