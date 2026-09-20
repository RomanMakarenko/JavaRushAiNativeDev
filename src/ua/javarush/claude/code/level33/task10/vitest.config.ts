import { defineConfig } from "vitest/config";

// Конфігурація тест-раннера: jsdom для рендеру компонентів, automatic JSX
// (без ручного import React), globals для автоочищення між тестами.
export default defineConfig({
  esbuild: {
    jsx: "automatic",
  },
  test: {
    globals: true,
    environment: "jsdom",
  },
});