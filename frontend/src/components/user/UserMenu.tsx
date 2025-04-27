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
  const queryClient = useQueryClient();

  const handleProfileClick = () => {
    navigate("/profile");
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
          My Profile
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleLogout}>Logout</DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
