import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "@/stores/userStore";
import { useQueryClient } from "@tanstack/react-query";


interface UserMenuProps {
  avatarUrl?: string;
  userInitials?: string;
}

export function UserMenu({ avatarUrl, userInitials = "UU" }: UserMenuProps) {
  const navigate = useNavigate();
  const clearUser = useUserStore((state) => state.clearUser);
  const user = useUserStore((state) => state.user);
  const queryClient = useQueryClient();

  // Check if user is a teacher or admin
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

  const handleProfileClick = () => {
    navigate("/profile");
  };

  const handleTeacherDashboardClick = () => {
    navigate("/teacher-dashboard");
  };

  const handleLogout = () => {
    localStorage.removeItem("authToken");
    clearUser();
    queryClient.clear();
    navigate("/login");
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Avatar className="cursor-pointer hover:ring-2 hover:ring-primary transition">
          <AvatarImage
            src={avatarUrl}
            alt="User avatar"
            onError={e => (e.currentTarget.src)}
          />
          <AvatarFallback>{userInitials}</AvatarFallback>
        </Avatar>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-64 mt-2 rounded-xl shadow-lg border border-blue-100 bg-white/95 p-0" align="end">
        {/* User Card */}
        <div className="flex flex-col items-center gap-1 px-4 py-4 border-b border-blue-50 bg-gradient-to-r from-blue-50 to-purple-50 rounded-t-xl">
          <Avatar className="h-12 w-12 mb-1">
            <AvatarImage
              src={avatarUrl}
              alt="User avatar"
              onError={e => (e.currentTarget.src)}
            />
            <AvatarFallback>{userInitials}</AvatarFallback>
          </Avatar>
          <div className="font-semibold text-blue-900 text-base">{user?.firstName}</div>
          <div className="text-xs text-blue-600 opacity-80">{user?.email}</div>
        </div>
        <div className="py-2">
          <DropdownMenuItem onClick={handleProfileClick} className="px-4 py-2 rounded hover:bg-blue-50 cursor-pointer font-medium text-blue-900">
            Dashboard
          </DropdownMenuItem>
          <DropdownMenuItem onClick={handleProfileClick} className="px-4 py-2 rounded hover:bg-blue-50 cursor-pointer font-medium text-blue-900">
            My Profile
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem asChild className="px-4 py-2 rounded hover:bg-blue-50 cursor-pointer font-medium text-blue-900">
            <a href="/curriculum">Curriculum</a>
          </DropdownMenuItem>
          {isTeacher && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleTeacherDashboardClick} className="px-4 py-2 rounded hover:bg-blue-50 cursor-pointer font-medium text-blue-900">
                Teacher Dashboard
              </DropdownMenuItem>
            </>
          )}
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleLogout} className="px-4 py-2 rounded hover:bg-red-50 cursor-pointer font-medium text-red-700">
            Logout
          </DropdownMenuItem>
        </div>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
