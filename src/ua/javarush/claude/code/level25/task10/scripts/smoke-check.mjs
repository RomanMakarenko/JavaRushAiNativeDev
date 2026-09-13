// Smoke-перевірка core flow: додавання страви до плану дня.
// Опора дорожньої карти для відтворюваної перевірки контрольної точки.

import { addMeal } from "../src/mealPlan.mjs";

const empty = {};
const after = addMeal(empty, "mon", "oatmeal");

if (!Array.isArray(after.mon) || after.mon[0] !== "oatmeal") {
  console.error("SMOKE FAIL: страва не додалася до плану дня");
  process.exit(1);
}

console.log("SMOKE OK: core flow додавання страви працює");