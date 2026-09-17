import { render, screen, fireEvent } from "@testing-library/react";
import Page from "../app/page";

describe("landing form", () => {
  it("вимикає Analyze для порожнього або пробільного URL", () => {
    render(<Page />);
    const input = screen.getByLabelText("campaign-url");
    const button = screen.getByRole("button", { name: "Analyze" });

    expect(button).toBeDisabled();
    fireEvent.change(input, { target: { value: "   " } });
    expect(button).toBeDisabled();
  });

  it("активує Analyze після введення непорожнього URL і показує звіт", () => {
    render(<Page />);
    const input = screen.getByLabelText("campaign-url");
    const button = screen.getByRole("button", { name: "Analyze" });

    fireEvent.change(input, { target: { value: "https://example.com" } });
    expect(button).toBeEnabled();
    fireEvent.click(button);
    expect(screen.getByRole("status")).toHaveTextContent("https://example.com");
  });
});