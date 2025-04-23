import apiClient from "@/api/apiClient";
import { fetchProtectedCurrentUser } from "@/api/user";
import { useUserStore } from "@/stores/userStore";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

export default function OAuthSuccess() {
  const [params] = useSearchParams();
  const token = params.get("token");
  const setUser = useUserStore((state) => state.setUser);
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      localStorage.setItem("authToken", token);
      apiClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      fetchProtectedCurrentUser()
        .then((user) => {
          setUser(user); 
          navigate("/");
        })
        .catch((err) => {
          console.error("Failed to fetch user after login", err);
          localStorage.removeItem("authToken");
          navigate("/login");
        });
    } else {
      navigate("/login");
    }
  }, [token, setUser, navigate]);

  return <>Logging in...</>;
}
