import React from "react";

// Властивості бейджа ручної перевірки повернення.
type RefundReviewBadgeProps = {
  // Ознака того, що повернення потребує ручної перевірки.
  requiresManualReview: boolean;
};

export function RefundReviewBadge({ requiresManualReview }: RefundReviewBadgeProps) {
  if (!requiresManualReview) {
    return null;
  }

  return <span>Manual review required</span>;
}