import { useQuery } from "@tanstack/react-query";
import { fetchHomework } from "../api/homework";

export default function Homework() {
    const { data: homework, isLoading, isError } = useQuery({
        queryKey: ['homework'],
        queryFn: fetchHomework,
      });
    
      if (isLoading) return <div className="p-6">Loading...</div>;
      if (isError) return <div className="p-6 text-red-500">Error fetching user info</div>;
    
      return (
        <div className="p-8">
          <h1 className="text-2xl font-bold mb-4">User Info</h1>
          <div className="space-y-2 text-lg">
            <p><strong>Subject:</strong> {homework.title}</p>
            <p><strong>Task:</strong> {homework.content}</p>
          </div>
        </div>
      );
}