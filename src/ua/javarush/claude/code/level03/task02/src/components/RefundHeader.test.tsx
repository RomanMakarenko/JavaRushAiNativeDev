// Тест перевіряє, що заголовок форми повернення показує коректний текст.
import React from "react";
import { render, screen } from "@testing-library/react";
import { RefundHeader } from "./RefundHeader";

test("відображає коректний заголовок форми повернення", () => {
  render(<RefundHeader />);
  expect(screen.getByText("Refund request")).toBeInTheDocument();
});