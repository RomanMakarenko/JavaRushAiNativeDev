# Prompt: розбір вхідного issue (варіант 2)

Майже те саме, що й перший варіант, але формулювання трохи інші.
По суті це той самий workflow: із bug report робимо engineering brief.

## Вхід
- Текст issue.
- Логи або повідомлення про помилку, якщо додані.

## Кроки
1. Відокремити факти від гіпотез.
2. Зафіксувати current vs desired behavior.
3. Знайти ймовірну зачеплену область.
4. Написати acceptance criteria і план перевірки.

## Вихід
markdown TASK_SPEC: Goal, Scope, Acceptance criteria, Verification.
Код не чіпати, тільки специфікація.