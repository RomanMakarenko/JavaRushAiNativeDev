---
name: issue-analysis
description: Перетворює вхідний issue Commerce OS на стандартний TASK_SPEC.md за шаблоном команди.
---

# Issue Analysis

Цей project-local skill живе в `.claude/skills/` репозиторію Commerce OS і
викликається як `/issue-analysis`. Він не входить до продукту Claude Code і доступний
лише в тому проєкті, де лежить цей файл.

## Що робить

Бере сирий текст issue (баг, невелика функція, запит від бізнесу) і збирає з нього
`TASK_SPEC.md` з такими розділами: Goal, Scope, Non-goals, Evidence, Open
questions.

## Коли використовувати

Коли в трекер надходить новий issue і потрібно швидко привести його до єдиного
інженерного вигляду перед плануванням роботи.