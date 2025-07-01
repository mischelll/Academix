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
          <AvatarImage src={avatarUrl + ""} alt="User avatar" />
          <AvatarFallback>{userInitials}</AvatarFallback>
        </Avatar>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-48 mt-2" align="end">
        <DropdownMenuItem onClick={handleProfileClick}>
          Dashboard
        </DropdownMenuItem>
        <DropdownMenuItem onClick={handleProfileClick}>
          My Profile
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleProfileClick}>
          Curriculum
        </DropdownMenuItem>
        {isTeacher && (
          <>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={handleTeacherDashboardClick}>
              Teacher Dashboard
            </DropdownMenuItem>
          </>
        )}
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleLogout}>
          Logout
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
