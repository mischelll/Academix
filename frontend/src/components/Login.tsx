export default function Login() {
      return (
        <div className="p-8">
          <h1 className="text-2xl font-bold mb-4">Login</h1>
          <div className="space-y-2 text-lg">
            <a href="http://localhost:8081/oauth2/authorization/google" className="bg-red-500 text-white px-4 py-2 rounded">
              Login with Google
             </a>
          </div>
        </div>
      );
}