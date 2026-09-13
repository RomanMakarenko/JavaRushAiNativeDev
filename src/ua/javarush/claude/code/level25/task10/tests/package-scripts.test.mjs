import { test } from "node:test";
import assert from "node:assert/strict";
import { readFileSync } from "node:fs";

const pkg = JSON.parse(readFileSync(new URL("../package.json", import.meta.url)));

test("у проєкті є скрипт test", () => {
  assert.ok(pkg.scripts && typeof pkg.scripts.test === "string");
});

test("скрипт smoke запускає перевірку явним шляхом", () => {
  assert.equal(pkg.scripts?.smoke, "node scripts/smoke-check.mjs");
});