import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { RefundReviewBadge } from "./RefundReviewBadge";

describe("RefundReviewBadge", () => {
  it("показує badge, коли requiresManualReview === true", () => {
    render(<RefundReviewBadge requiresManualReview={true} />);
    expect(screen.getByText("Manual review required")).toBeInTheDocument();
  });

  it("ховає badge, коли requiresManualReview === false", () => {
    render(<RefundReviewBadge requiresManualReview={false} />);
    expect(screen.queryByText("Manual review required")).not.toBeInTheDocument();
  });
});