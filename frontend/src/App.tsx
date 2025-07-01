import { useNavigate } from "react-router-dom";
import AppRoutes from "./AppRoutes";
import Navbar from "./components/layout/Navbar";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useRef } from "react";
import { fetchProtectedCurrentUser, } from "./api/user";
import { useUserStore } from "./stores/userStore";
import { Toaster } from "./components/ui/sonner";
import { backfillHomeworksForStudent } from "./api/curriculum";

export default function App() {
  const setUser = useUserStore((state) => state.setUser);
  const navigate = useNavigate();
  const backfillCalledRef = useRef<Set<number>>(new Set());

  const token = localStorage.getItem("authToken");

  const { data: user, isError } = useQuery({
    queryKey: ["currentUser"],
    queryFn: fetchProtectedCurrentUser,
    enabled: !!token, // only try if token exists
    retry: false,
  });

  useEffect(() => {
    if (user) {
      setUser(user);
      // Backfill homeworks for student (only once per user)
      if (user.roles?.some(role => role.name === 'ROLE_STUDENT') && !backfillCalledRef.current.has(user.id)) {
        backfillCalledRef.current.add(user.id);
        backfillHomeworksForStudent(user.id);
      }
    }
    if (isError) {
      localStorage.removeItem("authToken");
      navigate("/login");
    }
  }, [user, isError, setUser, navigate]);

  return (
    <>
      <Navbar />
      <main className="bg-gray-50 min-h-screen p-6">
        <AppRoutes />
      </main>
      <Toaster />
    </>
  );
}
