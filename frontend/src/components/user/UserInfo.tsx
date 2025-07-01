import { Role, useUserStore } from "@/stores/userStore";
import { toast } from "sonner";
import { Button } from "../ui/button";
import { Badge } from "../ui/badge";

export default function UserInfo() {
  const user = useUserStore((state) => state.user);

  // Check if user is a teacher or admin
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">User Info</h1>
      <div className="space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p className="text-sm font-medium text-gray-500">Username</p>
            <p className="text-lg">{user?.username}</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-500">Email</p>
            <p className="text-lg">{user?.email}</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-500">Name</p>
            <p className="text-lg">{user?.firstName}</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-500">Phone</p>
            <p className="text-lg">{user?.phone}</p>
          </div>
        </div>
        
        <div>
          <p className="text-sm font-medium text-gray-500 mb-2">Roles</p>
          <div className="flex flex-wrap gap-2">
            {user?.roles?.map((role: Role, index: number) => (
              <Badge key={index} variant={role.name.includes('ADMIN') ? 'destructive' : role.name.includes('TEACHER') ? 'default' : 'secondary'}>
                {role.name}
              </Badge>
            ))}
          </div>
        </div>

        <div>
          <p className="text-sm font-medium text-gray-500 mb-2">Role Status</p>
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <Badge variant={isTeacher ? 'default' : 'outline'}>
                {isTeacher ? '✓' : '✗'} Teacher Access
              </Badge>
              <span className="text-sm text-gray-600">
                {isTeacher ? 'Can access Teacher Dashboard' : 'No teacher privileges'}
              </span>
            </div>
            <div className="flex items-center gap-2">
              <Badge variant={user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? 'destructive' : 'outline'}>
                {user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? '✓' : '✗'} Admin Access
              </Badge>
              <span className="text-sm text-gray-600">
                {user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? 'Has admin privileges' : 'No admin privileges'}
              </span>
            </div>
          </div>
        </div>

        <div className="pt-4">
          <Button
            variant="outline"
            onClick={() =>
              toast("Update user event", {
                description: "Sunday, December 03, 2023 at 9:00 AM",
                action: {
                  label: "Close",
                  onClick: () => console.log("Undo"),
                },
              })
            }
          >
            Update Info
          </Button>
        </div>
      </div>
    </div>
  );
}
