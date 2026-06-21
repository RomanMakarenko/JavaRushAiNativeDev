# BOUNDARY_SPEC — Refund Manual Review Badge

## Scope

На сторінці деталей повернення (Refund Detail Page) додати візуальний індикатор — badge з текстом "Manual review required", який показується, коли поле `requiresManualReview` об'єкта refund дорівнює `true`.

Зміни обмежені фронтенд-шарем:
- Компонент `RefundReviewBadge` та його стилі.
- Тест компонента `RefundReviewBadge.test.tsx`.

## Non-goals

Наступні компоненти **не входять** в обсяг цієї задачі:

- **Backend**: жодних змін на серверній частині не потрібно. Поле `requiresManualReview` вже присутнє в об'єкті refund.
- **Payload / API**: формат відповіді API, mock-дані та типи даних не змінюються.
- **Інші компоненти сторінки**: зміни вносяться лише в `RefundReviewBadge`. Жодні інші компоненти Refund Detail Page не модифікуються.
- **Маршрутизація / сторінки**: додавання або зміна роутів, layout-ів чи сторінок не передбачена.
- **Інфраструктура**: збірка, деплой, CSS-фреймворки, тестове оточення не змінюються.

## Affected area

- `src/components/RefundReviewBadge.tsx` — реалізація компонента.
- `src/components/RefundReviewBadge.test.tsx` — тести на відображення (`true`) і приховування (`false`) badge.