import { Link } from "react-router-dom";
import { UserMenu } from "../user/UserMenu";
import { useUserStore } from "@/stores/userStore";
import logo from "/vite.svg";
import defaultAvatar from "@/assets/default-avatar.svg?url";

export default function Navbar() {
  const user = useUserStore((state) => state.user);

  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );
  const isStudent = user?.roles?.some(role => role.name === 'ROLE_STUDENT');

  const isLoggedIn = !!user;

  return (
    <nav className="bg-gray-800">
      <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
        <div className="relative flex h-16 items-center justify-between">
          <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
            <div className="flex shrink-0 items-center">
              <Link to="/" className="flex items-center gap-2 rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white">
                <img src={logo} alt="Academix Logo" className="h-8 w-auto" />
                <span className="sr-only">Academix</span>
              </Link>
            </div>
            {isLoggedIn && (
              <div className="hidden sm:ml-6 sm:block">
                <div className="flex space-x-4">
                  <Link
                    to="/home"
                    className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                  >
                    Home
                  </Link>
                  {isStudent && (
                    <Link
                      to="/assignments"
                      className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                    >
                      Assignments
                    </Link>
                  )}
                  <Link
                    to="/curriculum"
                    className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                  >
                    Curriculum
                  </Link>
                  {isTeacher && (
                    <Link
                      to="/teacher-dashboard"
                      className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                    >
                      Teacher Dashboard
                    </Link>
                  )}
                </div>
              </div>
            )}
          </div>
          <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
            {isLoggedIn ? (
              <UserMenu avatarUrl={user?.avatar || defaultAvatar} userInitials={user?.firstName?.slice(0, 2).toUpperCase() ?? "UU"} />
            ) : (
              <Link
                to="/login"
                className="ml-2 px-5 py-2 rounded-lg bg-gradient-to-r from-blue-600 to-purple-500 text-white font-semibold shadow hover:from-blue-700 hover:to-purple-600 transition-all"
              >
                Login
              </Link>
            )}
          </div>
        </div>
      </div>

      <div className="sm:hidden" id="mobile-menu">
        <div className="space-y-1 px-2 pt-2 pb-3">
          <Link to="/" className="flex items-center gap-2 rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white">
            <img src={logo} alt="Academix Logo" className="h-8 w-auto" />
            <span className="sr-only">Academix</span>
          </Link>
          {isLoggedIn && (
            <div className="flex items-center gap-6 text-sm">
              <Link
                to="/home"
                className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
              >
                Home
              </Link>
              {isStudent && (
                <Link
                  to="/assignments"
                  className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                >
                  Assignments
                </Link>
              )}
              {isTeacher && (
                <Link
                  to="/teacher-dashboard"
                  className="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white"
                >
                  Teacher Dashboard
                </Link>
              )}
            </div>
          )}
          {!isLoggedIn && (
            <div className="flex items-center gap-6 text-sm mt-2">
              <Link
                to="/login"
                className="w-full block text-center px-5 py-2 rounded-lg bg-gradient-to-r from-blue-600 to-purple-500 text-white font-semibold shadow hover:from-blue-700 hover:to-purple-600 transition-all"
              >
                Login
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
