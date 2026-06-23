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
 */
export function NewsletterForm({ onSubmit }: NewsletterFormProps) {
  const [email, setEmail] = useState("");
  const [frequency, setFrequency] = useState<Frequency>("weekly");

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    onSubmit({ email, frequency: "weekly" });
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