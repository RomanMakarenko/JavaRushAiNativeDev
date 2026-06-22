import { useState } from "react";

export type Frequency = "daily" | "weekly";

export interface NewsletterPayload {
  email: string;
  frequency: Frequency;
}

interface NewsletterFormProps {
  onSubmit: (payload: NewsletterPayload) => void;
}

/**
 * Форма підписки на розсилку ShopFlow.
 * Bugfix WEB-208 уже застосовано в робочому дереві: у payload передається
 * вибрана користувачем частота, а не захардкожене "weekly".
 */
export function NewsletterForm({ onSubmit }: NewsletterFormProps) {
  const [email, setEmail] = useState("");
  const [frequency, setFrequency] = useState<Frequency>("weekly");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    // Змінений рядок bugfix: було frequency: "weekly" (захардкожено),
    // стало frequency: frequency (значення зі state вибору користувача).
    onSubmit({ email, frequency });
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        aria-label="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <select
        aria-label="frequency"
        value={frequency}
        onChange={(e) => setFrequency(e.target.value as Frequency)}
      >
        <option value="daily">daily</option>
        <option value="weekly">weekly</option>
      </select>
      <button type="submit">Subscribe</button>
    </form>
  );
}