// Production-код сервісу перевірки здоров'я checkout-service.
// За умовою задачі цей файл змінювати не можна.

export interface HealthStatus {
  ok: boolean;
  service: string;
}

// Повертає статус готовності сервісу.
export function getHealth(service: string): HealthStatus {
  return { ok: true, service };
}