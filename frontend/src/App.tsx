import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { fetchProtectedCurrentUser } from "./api/user";
import AppRoutes from "./AppRoutes";
import Navbar from "./components/layout/Navbar";
import { useUserStore } from "./stores/userStore";

export default function App() {
  const setUser = useUserStore((state) => state.setUser);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    if (!token) return;

    fetchProtectedCurrentUser()
      .then((user) => {
        setUser(user);
      })
      .catch(() => {
        localStorage.removeItem("authToken");
        navigate("/login");
      });
  }, [setUser, navigate]);
  return (
    <>
      <Navbar />
      <main className="bg-gray-50 min-h-screen p-6">
        <AppRoutes />
      </main>
    </>
  );
}
