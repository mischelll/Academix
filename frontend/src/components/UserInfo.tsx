import { useQuery } from "@tanstack/react-query";
import { fetchProtectedCurrentUser } from "../api/user";

export default function UserInfo() {
    const { data: user, isLoading, isError } = useQuery({
        queryKey: ['currentUser'],
        queryFn: fetchProtectedCurrentUser,
      });
    
      if (isLoading) return <div className="p-6">Loading...</div>;
      if (isError) return <div className="p-6 text-red-500">Error fetching user info</div>;
    
      return (
        <div className="p-8">
          <h1 className="text-2xl font-bold mb-4">User Info</h1>
          <div className="space-y-2 text-lg">
            <p><strong>Username:</strong> {user.username}</p>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Name:</strong> {user.firstName}</p>
          </div>
        </div>
      );
}