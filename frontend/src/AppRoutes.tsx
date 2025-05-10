import { Routes, Route, useNavigate } from "react-router-dom";
import OAuthSuccess from "./components/OAuthSuccess";
import Login from "./components/Login";
import UserInfo from "./components/user/UserInfo";
import { useEffect } from "react";
import { setNavigator } from "./api/navigation";
import Homework from "./components/homework/Homework";
import Home from "./components/Home";
import Major from "./components/curriculum/Major";
import Semester from "./components/curriculum/Semester";
import Course from "./components/curriculum/Course";
import Lesson from "./components/curriculum/Lesson";
import CurriculumLayout from "./components/curriculum/CurriculumLayout";

export default function AppRoutes() {
  const navigate = useNavigate();

  useEffect(() => {
    setNavigator(navigate);
  }, [navigate]);

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/profile" element={<UserInfo />} />
      <Route path="/assignments" element={<Homework />} />
      <Route
        path="/curriculum"
        element={<CurriculumLayout />}
      >
        <Route path="majors" element={<Major />} />
        <Route path="majors/:majorId/semesters" element={<Semester />} />
        <Route path="semesters/:semesterId/courses" element={<Course />} />
        <Route path="courses/:courseId/lessons" element={<Lesson />} />
      </Route>
      {/* Security */}
      <Route path="/oauth-success" element={<OAuthSuccess />} />
    </Routes>
  );
}
