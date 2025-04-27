import { Routes, Route, useNavigate } from "react-router-dom";
import OAuthSuccess from "./components/OAuthSuccess";
import Login from "./components/Login";
import UserInfo from "./components/user/UserInfo";
import { useEffect } from "react";
import { setNavigator } from "./api/navigation";
import Homework from "./components/homework/Homework";
import Home from "./components/Home";

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
      <Route path="/oauth-success" element={<OAuthSuccess />} />
    </Routes>
  );
}
