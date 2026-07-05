---
name: issue-analysis
description: Перетворює вхідний issue на перевірний TASK_SPEC.md без початку реалізації.
---

# Issue Analysis

Цей skill приймає issue з модуля підтримки Commerce OS і готує
короткий `TASK_SPEC.md`: мета, область змін, не-цілі, критерії приймання
та план перевірки. Розпочинати реалізацію не можна — це аналітичний артефакт.

## Коли використовувати
Коли надійшов новий issue і потрібен структурований спек перед плануванням.

## Дії
1. Прочитай issue.
2. Знайди 2-5 найбільш релевантних файлів.
3. Заповни `templates/TASK_SPEC.template.md`.
4. Відокрем факти від припущень.
5. Поверни TASK_SPEC.md без початку реалізації.

## Supporting files
- Шаблон артефакта: `templates/TASK_SPEC.template.md`
- Приклад заповнення: `examples/refund-sorting.md`

## Output contract
На виході — заповнений `TASK_SPEC.md` за структурою шаблону, з розділами
`Goal`, `Scope`, `Non-goals`, `Acceptance criteria`, `Verification`
та `Open questions`.

## Обмеження
- Не починати реалізацію.
- Не тягнути в контекст увесь репозиторій.
- Припущення позначати явно.