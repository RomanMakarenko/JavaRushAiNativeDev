import { readFile } from "node:fs/promises";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";

import { buildMonthlyReport } from "../src/lib/buildMonthlyReport.ts";

const projectRoot = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const sampleDataPath = resolve(projectRoot, "data/sample-transactions.json");
const transactions = JSON.parse(await readFile(sampleDataPath, "utf8"));
const report = buildMonthlyReport(transactions);

if (!report.month || report.totalIncome <= 0 || report.totalExpense <= 0) {
  throw new Error("Smoke-перевірка місячного звіту не пройдена");
}

console.log("CORE_FLOW_OK");