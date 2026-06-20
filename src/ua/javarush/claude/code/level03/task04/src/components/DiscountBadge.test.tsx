import React from "react";
import { render, screen } from "@testing-library/react";
import { DiscountBadge } from "./DiscountBadge";

describe("DiscountBadge", () => {
  it("показує додатну знижку зі знаком відсотка", () => {
    render(<DiscountBadge discountPercent={25} />);
    expect(screen.getByText("25%")).toBeInTheDocument();
  });

  it("показує невелику додатну знижку", () => {
    render(<DiscountBadge discountPercent={10} />);
    expect(screen.getByText("10%")).toBeInTheDocument();
  });
});