import { useSemesters } from "@/hooks/useSemesters";
import { useParams } from "react-router-dom";

export type Semester = {
    id: number;
    name: string;
  };

function Semester() {
const { majorId } = useParams();
const { data: semesters, isLoading } = useSemesters(majorId!);

if (isLoading) return <p>Loading...</p>;

    return (  
        <>
            <ul>
                {semesters.map((semester: Semester) => {
                    <li key={semester.id}>{semester.name}</li>
                })}    
            </ul> 
        </>
    );
}

export default Semester;