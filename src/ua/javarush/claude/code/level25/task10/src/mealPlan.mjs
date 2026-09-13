// Ядро core flow: додавання страви до плану дня.
// Це єдиний сценарій першої версії capstone-проєкту.

/**
 * Додає страву до плану конкретного дня.
 * @param {Object} plan - поточний план тижня, ключ = день, значення = масив страв
 * @param {string} day - день тижня (наприклад "mon")
 * @param {string} meal - назва страви
 * @returns {Object} новий план з доданою стравою
 */
export function addMeal(plan, day, meal) {
  if (!day || !meal) {
    throw new Error("day і meal обов'язкові");
  }
  const current = plan[day] ?? [];
  return { ...plan, [day]: [...current, meal] };
}