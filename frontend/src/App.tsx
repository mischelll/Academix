import { useNavigate } from "react-router-dom";
import AppRoutes from "./AppRoutes";
import Navbar from "./components/layout/Navbar";
import { useQuery } from "@tanstack/react-query";
import { useEffect } from "react";
import { fetchProtectedCurrentUser } from "./api/user";
import { useUserStore } from "./stores/userStore";
import { Toaster } from "./components/ui/sonner";

export default function App() {
  const setUser = useUserStore((state) => state.setUser);
  const navigate = useNavigate();

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
