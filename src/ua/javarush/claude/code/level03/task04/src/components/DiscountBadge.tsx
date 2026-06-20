import React from "react";

type DiscountBadgeProps = {
  // Розмір знижки у відсотках (0..100)
  discountPercent: number;
};

// Бейдж знижки на картці товару у вітрині Commerce OS.
export function DiscountBadge({ discountPercent }: DiscountBadgeProps) {
  // БАГ: truthiness-перевірка вважає значення 0 «порожнім»,
  // тому для нульової знижки бейдж не відображається.
  if (!discountPercent) {
    return null;
  }

  return <span className="discount-badge">{discountPercent}%</span>;
}