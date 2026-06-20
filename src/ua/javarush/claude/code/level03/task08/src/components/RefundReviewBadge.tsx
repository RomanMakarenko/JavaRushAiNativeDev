import React from "react";

// Властивості бейджа ручної перевірки повернення.
type RefundReviewBadgeProps = {
  // Ознака того, що повернення потребує ручної перевірки.
  requiresManualReview: boolean;
};

// TODO: показати badge "Manual review required", коли requiresManualReview === true,
// і нічого не показувати, коли значення false.
export function RefundReviewBadge(_props: RefundReviewBadgeProps) {
  return null;
}