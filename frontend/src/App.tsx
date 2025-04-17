import { BrowserRouter as Router} from 'react-router-dom'
import './App.css'
import AppRoutes from './AppRoutes'
import Navbar from './components/layout/Navbar';

export default function App() {
  return (
    <Router>
      <Navbar />
      <main className="bg-gray-50 min-h-screen p-6">
        <AppRoutes />

      </main>
    </Router>
  );
}
