import { render, screen, fireEvent } from "@testing-library/react";
import Page from "../app/page";

// Базовий тест на основний сценарій форми.
describe("landing form", () => {
  it("показує звіт після введення URL та натискання Analyze", () => {
    render(<Page />);
    const input = screen.getByLabelText("campaign-url");
    fireEvent.change(input, { target: { value: "https://example.com" } });
    fireEvent.click(screen.getByText("Analyze"));
    expect(screen.getByRole("status")).toHaveTextContent("https://example.com");
  });
});