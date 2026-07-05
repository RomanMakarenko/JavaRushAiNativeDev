---
name: issue-analysis
description: Перетворює вхідний issue на перевірюваний TASK_SPEC.md без початку реалізації.
---

# Issue Analysis

Цей skill приймає issue з модуля підтримки Commerce OS і готує
короткий `TASK_SPEC.md`: мета, область змін, нецілі, критерії приймання
та план перевірки. Починати реалізацію не можна — це аналітичний артефакт.

## Коли використовувати
Коли надійшов новий issue і потрібна структурована специфікація перед плануванням.

## Дії
1. Прочитайте issue.
2. Знайдіть 2–5 найбільш релевантних файлів.
3. Заповніть [`templates/TASK_SPEC.template.md`](templates/TASK_SPEC.template.md) — це єдиний зразок.
4. Відокремте факти від припущень.
5. Поверніть `TASK_SPEC.md` без початку реалізації.

## Ресурси
- **Шаблон**: [`templates/TASK_SPEC.template.md`](templates/TASK_SPEC.template.md) — обов'язковий формат вихідного файлу.
- **Приклад**: [`examples/refund-sorting.md`](examples/refund-sorting.md) — заповнений TASK_SPEC для RF-217 (сортування повернень).

## Output contract
Вихідний артефакт — `TASK_SPEC.md`, заповнений за шаблоном. Містить Goal, Scope, Non-goals, Acceptance criteria, Verification, Open questions. Реалізацію починати заборонено.

## Обмеження
- Не починати реалізацію.
- Не тягнути в контекст увесь репозиторій.
- Припущення позначати явно.