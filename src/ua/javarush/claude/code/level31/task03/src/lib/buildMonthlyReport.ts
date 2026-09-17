// Ядро core flow: зі списку транзакцій будує місячний звіт.
// Використовується і UI-сторінкою звіту, і smoke-скриптом.

export type Transaction = {
  date: string;        // ISO-дата транзакції, наприклад "2026-05-04"
  description: string; // опис із виписки
  category: string;    // категорія витрати/доходу
  amount: number;      // додатна сума — дохід, від'ємна — витрата
};

export type MonthlyReport = {
  month: string;                       // "YYYY-MM"
  totalIncome: number;                 // сума всіх додатних amount
  totalExpense: number;                // сума всіх від'ємних amount (за модулем)
  byCategory: Record<string, number>;  // витрати за категоріями (за модулем)
};

// Будує місячний звіт зі списку транзакцій.
// Кидає помилку, якщо на вхід надійшов порожній список — звіт будувати ні з чого.
export function buildMonthlyReport(transactions: Transaction[]): MonthlyReport {
  if (!transactions || transactions.length === 0) {
    throw new Error("Немає транзакцій: місячний звіт будувати ні з чого");
  }

  const month = transactions[0].date.slice(0, 7);
  let totalIncome = 0;
  let totalExpense = 0;
  const byCategory: Record<string, number> = {};

  for (const tx of transactions) {
    if (tx.amount >= 0) {
      totalIncome += tx.amount;
    } else {
      const spent = Math.abs(tx.amount);
      totalExpense += spent;
      byCategory[tx.category] = (byCategory[tx.category] ?? 0) + spent;
    }
  }

  return { month, totalIncome, totalExpense, byCategory };
}