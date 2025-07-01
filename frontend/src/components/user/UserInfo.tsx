import { Role, useUserStore } from "@/stores/userStore";
import { toast } from "sonner";
import { Button } from "../ui/button";
import { Badge } from "../ui/badge";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import defaultAvatar from "@/assets/default-avatar.svg?url";

export default function UserInfo() {
  const user = useUserStore((state) => state.user);

  // Check if user is a teacher or admin
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-purple-100 py-8">
      <div className="w-full max-w-xl bg-white/90 rounded-2xl shadow-xl border border-blue-100 p-8 flex flex-col items-center gap-6">
        <Avatar className="h-20 w-20 mb-2 shadow">
          <AvatarImage
            src={user?.avatar || defaultAvatar}
            alt="User avatar"
            onError={e => (e.currentTarget.src = defaultAvatar)}
          />
          <AvatarFallback>{user?.firstName?.slice(0, 2).toUpperCase() ?? "UU"}</AvatarFallback>
        </Avatar>
        <div className="text-center">
          <h1 className="text-3xl font-extrabold text-blue-900 mb-1">{user?.firstName}</h1>
          <div className="text-blue-700 text-md mb-2">{user?.email}</div>
        </div>
        <div className="w-full grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p className="text-xs font-medium text-gray-500">Username</p>
            <p className="text-lg font-semibold text-blue-900">{user?.username}</p>
          </div>
          <div>
            <p className="text-xs font-medium text-gray-500">Phone Number</p>
            <p className="text-lg font-semibold text-blue-900">{user?.phone || <span className="text-gray-400">Not provided</span>}</p>
          </div>
          <div>
            <p className="text-xs font-medium text-gray-500">City</p>
            <p className="text-lg font-semibold text-blue-900">{user?.city || '-'}</p>
          </div>
          <div>
            <p className="text-xs font-medium text-gray-500">Verified</p>
            <p className="text-lg font-semibold text-blue-900">{user?.isVerified ? 'Yes' : 'No'}</p>
          </div>
        </div>
        <div className="w-full">
          <p className="text-xs font-medium text-gray-500 mb-2">Roles</p>
          <div className="flex flex-wrap gap-2">
            {user?.roles?.map((role: Role, index: number) => (
              <Badge key={index} variant={role.name.includes('ADMIN') ? 'destructive' : role.name.includes('TEACHER') ? 'default' : 'secondary'} className="text-base px-3 py-1">
                {role.name.replace('ROLE_', '')}
              </Badge>
            ))}
          </div>
        </div>
        <div className="w-full">
          <p className="text-xs font-medium text-gray-500 mb-2">Access</p>
          <div className="flex flex-col gap-2">
            <div className="flex items-center gap-2">
              <Badge variant={isTeacher ? 'default' : 'outline'} className="text-sm px-2 py-1">
                {isTeacher ? '✓' : '✗'} Teacher Access
              </Badge>
              <span className="text-sm text-gray-600">
                {isTeacher ? 'Can access Teacher Dashboard' : 'No teacher privileges'}
              </span>
            </div>
            <div className="flex items-center gap-2">
              <Badge variant={user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? 'destructive' : 'outline'} className="text-sm px-2 py-1">
                {user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? '✓' : '✗'} Admin Access
              </Badge>
              <span className="text-sm text-gray-600">
                {user?.roles?.some(role => role.name === 'ROLE_ADMIN') ? 'Has admin privileges' : 'No admin privileges'}
              </span>
            </div>
          </div>
        </div>
        <div className="pt-2 w-full flex justify-center">
          <Button
            variant="outline"
            className="px-6 py-2 text-blue-700 border-blue-300 hover:bg-blue-50 hover:text-blue-900"
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
