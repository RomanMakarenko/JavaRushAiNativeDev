import { describe, it, expect } from 'vitest';
import { getHealth } from '../src/health';

describe('getHealth', () => {
  it('повертає ok=true і ім\'я сервісу', () => {
    const status = getHealth('checkout-service');
    expect(status.ok).toBe(true);
    expect(status.service).toBe('checkout-service');
  });
});