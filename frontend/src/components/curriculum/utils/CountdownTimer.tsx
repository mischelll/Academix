import { useState, useEffect } from "react";

export default function CountdownTimer({ duration = 60, onExpire }: { duration?: number; onExpire?: () => void }) {
  const [timeLeft, setTimeLeft] = useState(duration);

  useEffect(() => {
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(interval);
          onExpire?.();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [onExpire]);

  const minutes = Math.floor(timeLeft / 60);
  const seconds = timeLeft % 60;

  return (
    <p className="text-sm text-muted-foreground">
      Time left: {minutes}:{seconds.toString().padStart(2, "0")}
    </p>
  );
}