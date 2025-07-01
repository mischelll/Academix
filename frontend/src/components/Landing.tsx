import { Link } from "react-router-dom";
import logo from "/vite.svg";

export default function Landing() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-blue-50 to-purple-100">
      <div className="flex flex-col items-center gap-6 p-8 rounded-xl shadow-xl bg-white/80 border border-blue-100">
        <img src={logo} alt="Academix Logo" className="h-20 w-20 mb-2 drop-shadow-lg" />
        <h1 className="text-5xl font-extrabold text-blue-900 tracking-tight text-center">Academix</h1>
        <p className="text-lg text-blue-700 text-center max-w-xl">
          The modern platform for managing assignments, curriculum, and student progress.<br/>
          Empowering students and teachers with seamless digital education tools.
        </p>
        <Link
          to="/login"
          className="mt-4 px-8 py-3 rounded-lg bg-gradient-to-r from-blue-600 to-purple-500 text-white text-lg font-semibold shadow-md hover:scale-105 hover:from-blue-700 hover:to-purple-600 transition-all"
        >
          Get Started
        </Link>
      </div>
      <div className="mt-10 text-center text-blue-400 text-sm opacity-80">
        &copy; {new Date().getFullYear()} Academix. All rights reserved.
      </div>
    </div>
  );
} 