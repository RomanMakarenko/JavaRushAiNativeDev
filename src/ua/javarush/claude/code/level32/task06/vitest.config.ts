import { defineConfig } from 'vitest/config';

// Конфігурація не бачить папку tests: include вказує лише на src,
// тому реальні тести з tests/ у прогін не потрапляють.
export default defineConfig({
  test: {
    include: ['tests/**/*.test.ts'],
  },
});