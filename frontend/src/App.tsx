import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import OAuthSuccess from './components/OAuthSuccess'
import Login from './components/Login'
import UserInfo from './components/UserInfo'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/home" element={<UserInfo />} />
        <Route path="/login" element={<Login />} />
        <Route path="/oauth-success" element={<OAuthSuccess />} />
      </Routes>
    </Router>
  )
}

export default App
