
import { Role, useUserStore } from "@/stores/userStore";

export default function UserInfo() {
  const user = useUserStore((state) => state.user);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">User Info</h1>
      <div className="space-y-2 text-lg">
        <p>
          <strong>Username:</strong> {user?.username}
        </p>
        <p>
          <strong>Email:</strong> {user?.email}
        </p>
        <p>
          <strong>Name:</strong> {user?.firstName}
        </p>
        <p>
          <strong>Roles [</strong>
          {user?.roles.map((role: Role) => {
            return (
              <span>
                {role?.name}
              </span>
            );
          })}
          <strong>]</strong>
        </p>
      </div>
    </div>
  );
}
