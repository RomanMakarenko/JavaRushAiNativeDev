---
name: pr-summary
description: Збирає короткий зрозумілий для людини опис pull request на основі git diff.
when_to_use: Коли гілка готова до рев’ю і потрібен чернетковий опис PR з реального diff.
arguments: Необов’язковий діапазон git diff (за замовчуванням — зміни відносно базової гілки).
allowed_tools:
  - read
  - bash(git diff:*)
  - bash(git log:*)
---

# pr-summary

Сформуй короткий опис pull request на основі реального git diff.

## Expected output

Markdown-опис PR з розділами «Що змінено», «Навіщо» і «Як перевірити».

## Constraints

- Працюй лише з фактичним diff, нічого не вигадуй.
- Не внось змін у код і не створюй коміти.