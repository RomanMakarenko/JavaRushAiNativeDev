# Карта ризиків модуля MRR

Документ описує зони ризику навколо розрахунку Monthly Recurring Revenue.
Використовується як вхід для планування маленьких безпечних refactor slice.

## Зони ризику

| Зона | Файл | Ризик | Примітка |
|------|------|------|---------|
| Пошук тарифу | `MrrCalculator.calculateMonthlyMrr` | середній | пошук плану вбудований прямо в розрахунок |
| Розрахунок суми | `MrrCalculator.calculateMonthlyMrr` | високий | прив'язаний до legacy-формули `price * seats` |
| Текст помилки | `MrrCalculator.calculateMonthlyMrr` | високий | рядок `Unknown plan code:` перевіряється тестами |
| Audit log | `MrrRefreshService.refresh` | середній | запис аудиту змішано з бізнес-логікою |

## Безпечні перші кроки

- Винести пошук тарифу в private helper `resolvePlan()` без зміни поведінки.
- Не чіпати публічну сигнатуру та текст виключення.
- Будь-який slice тримати маленьким і reviewable.