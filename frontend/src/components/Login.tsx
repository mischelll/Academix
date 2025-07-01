import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "@/stores/userStore";
import logo from "/vite.svg";

export default function Login() {
  const user = useUserStore((state) => state.user);
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      navigate("/home", { replace: true });
    }
  }, [user, navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-purple-100">
      <div className="flex flex-col items-center gap-6 p-8 rounded-xl shadow-xl bg-white/80 border border-blue-100 max-w-md w-full">
        <img src={logo} alt="Academix Logo" className="h-16 w-16 mb-2 drop-shadow-lg" />
        <h1 className="text-4xl font-extrabold text-blue-900 tracking-tight text-center">Academix Login</h1>
        <p className="text-md text-blue-700 text-center max-w-xl mb-2">
          Sign in to access your assignments, curriculum, and more.
        </p>
        <a
          href="http://localhost:8081/oauth2/authorization/google"
          className="mt-2 px-8 py-3 rounded-lg bg-gradient-to-r from-blue-600 to-purple-500 text-white text-lg font-semibold shadow-md hover:scale-105 hover:from-blue-700 hover:to-purple-600 transition-all flex items-center gap-2"
        >
          <svg className="h-6 w-6" viewBox="0 0 48 48"><g><path fill="#4285F4" d="M44.5 20H24v8.5h11.7C34.7 33.1 29.9 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c2.7 0 5.2.9 7.2 2.4l6.4-6.4C34.3 5.1 29.4 3 24 3 12.4 3 3 12.4 3 24s9.4 21 21 21c10.5 0 20-7.5 20-21 0-1.3-.1-2.7-.5-4z"/><path fill="#34A853" d="M6.3 14.7l7 5.1C15.5 16.2 19.4 13 24 13c2.7 0 5.2.9 7.2 2.4l6.4-6.4C34.3 5.1 29.4 3 24 3c-7.2 0-13.4 3.1-17.7 8.1z"/><path fill="#FBBC05" d="M24 45c5.4 0 10.3-1.8 14.1-4.9l-6.5-5.3C29.6 36.2 26.9 37 24 37c-5.8 0-10.7-3.9-12.5-9.2l-7 5.4C7.7 41.1 15.3 45 24 45z"/><path fill="#EA4335" d="M44.5 20H24v8.5h11.7c-1.2 3.2-4.7 7.5-11.7 7.5-6.6 0-12-5.4-12-12s5.4-12 12-12c2.7 0 5.2.9 7.2 2.4l6.4-6.4C34.3 5.1 29.4 3 24 3c-7.2 0-13.4 3.1-17.7 8.1z"/></g></svg>
          Login with Google
        </a>
      </div>
    </div>
  );
}