import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button"

export default function Navbar() {
    return (
      <nav className="w-full bg-white border-b shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        {/* Logo / Brand */}
        <Link to="/" className="text-xl font-bold text-blue-600">
          Academix
        </Link>

        {/* Links + Login */}
        <div className="flex items-center gap-6 text-sm">
          <Link to="/home" className="text-gray-700 hover:text-blue-600 transition-colors">
            Home
          </Link>
          <Link to="/profile" className="text-gray-700 hover:text-blue-600 transition-colors">
            Profile
          </Link>
          <Button asChild>
            <a href="http://localhost:8081/oauth2/authorization/google">Login</a>
          </Button>
        </div>
      </div>
    </nav>
    )
}