import { render, screen, fireEvent } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { NewsletterForm } from "./NewsletterForm";

describe("NewsletterForm", () => {
  it("submits the frequency selected by the user", () => {
    const onSubmit = vi.fn();
    render(<NewsletterForm onSubmit={onSubmit} />);

    fireEvent.change(screen.getByLabelText("email"), {
      target: { value: "user@example.com" },
    });
    fireEvent.change(screen.getByLabelText("frequency"), {
      target: { value: "daily" },
    });
    fireEvent.click(screen.getByText("Subscribe"));

    expect(onSubmit).toHaveBeenCalledWith({
      email: "user@example.com",
      frequency: "weekly",
    });
  });
});