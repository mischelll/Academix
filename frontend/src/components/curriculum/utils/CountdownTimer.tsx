import { useState, useEffect } from "react";

export default function CountdownTimer({
  duration = 60000, // duration in milliseconds
  onExpire,
}: {
  duration?: number;
  onExpire?: () => void;
}) {
  const [timeLeft, setTimeLeft] = useState(duration);

  useEffect(() => {
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1000) {
          clearInterval(interval);
          onExpire?.();
          return 0;
        }
        return prev - 1000;
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [onExpire]);

  const days = Math.floor(timeLeft / (1000 * 60 * 60 * 24));
  const hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);

  return (
    <p className="text-sm text-muted-foreground">
      Time left:{" "}
      {days > 0 && `${days} day${days > 1 ? "s" : ""} `}
      {hours > 0 && `${hours} hour${hours > 1 ? "s" : ""} `}
      {minutes > 0 && `${minutes} minute${minutes > 1 ? "s" : ""} `}
      {seconds >= 0 && `${seconds} second${seconds !== 1 ? "s" : ""}`}
    </p>
  );
}