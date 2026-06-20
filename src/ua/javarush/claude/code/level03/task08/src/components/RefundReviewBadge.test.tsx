import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { RefundReviewBadge } from "./RefundReviewBadge";

// TODO: додати перевірки на відображення і приховування badge
// залежно від значення requiresManualReview.
describe("RefundReviewBadge", () => {
  it("чернетка тесту", () => {
    render(<RefundReviewBadge requiresManualReview={false} />);
  });
});