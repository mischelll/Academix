import { Routes, Route, useNavigate } from 'react-router-dom';
import OAuthSuccess from './components/OAuthSuccess';
import Login from './components/Login';
import UserInfo from './components/UserInfo';
import { useEffect } from 'react';
import { setNavigator } from './api/navigation';

export default function AppRoutes() {
  const navigate = useNavigate();

  useEffect(() => {
    setNavigator(navigate); // ✅ now safe: inside <Router>
  }, [navigate]);

  return (
    <Routes>
        <Route path="/" element={<UserInfo />} />
      <Route path="/home" element={<UserInfo />} />
      <Route path="/login" element={<Login />} />
      <Route path="/oauth-success" element={<OAuthSuccess />} />
    </Routes>
  );
}