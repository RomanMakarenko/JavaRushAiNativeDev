# Proof of Work

Curated evidence from four distinct domains. Claims and links are limited to the supplied source materials.

## 1. Developer tooling — evidence bank tool

- **Problem:** Ручна збірка публічного proof-of-work із розрізнених артефактів забирала час і створювала ризик витоку приватних даних.
- **What I owned:** Дизайн фільтрації за visibility, набір обов’язкових секцій і тести на відсутність private fields.
- **AI role:** Допомога з чернеткою генератора і тестів під контролем розробника.
- **Verification:** `pytest` зелений; тест перевіряє, що private записи не потрапляють до збірки.
- **Result:** Інструмент збирає публічний документ лише з дозволених записів.
- **Links:** https://github.com/example/evidence-bank-demo
- **Limitations:** Підтримується лише Markdown-вивід; UI немає.

## 2. E-commerce checkout — duplicate discount fix

- **Problem:** За кількох знижок підсумкова сума кошика рахувалася двічі.
- **What I owned:** Локалізація бага в розрахунку `total`, мінімальне виправлення і regression test.
- **AI role:** Генерація кандидатів гіпотез; розробник обирав і перевіряв рішення.
- **Verification:** Додано regression test на duplicate discount; локальна збірка зелена.
- **Result:** `total` рахується коректно за будь-якої кількості знижок.
- **Links:** https://github.com/example/commerce/pull/142
- **Limitations:** Виправлення покриває лише сценарій знижок, а не весь pricing-движок.

## 3. Data and billing migration — MRR lookup

- **Problem:** Розрахунок MRR спирався на legacy-джерело, яке заважало подальшому розвитку.
- **What I owned:** Фазовий план `expand → backfill → switch → contract`, feature flags і rollback.
- **AI role:** Допомога у формулюванні плану та parity-перевірок під ревізією розробника.
- **Verification:** Передбачено parity test між legacy і v2 lookup; кожна фаза має окремий flag.
- **Result:** Підготовлено безпечний поетапний план міграції без втрати даних.
- **Links:** https://github.com/example/cashflow/blob/main/MIGRATION_PLAN.md
- **Limitations:** Документ описує план; фактичне виконання не підтверджене цим артефактом.

## 4. Developer workflow and automation — reviewer subagent

- **Problem:** Ручний Layer 2 review був непослідовним і пропускав надто широкі diff.
- **What I owned:** Дизайн ролі агента, обмеження tools до read і запуску тестів, а також формат output.
- **AI role:** Виконання review за заданим контрактом; рішення приймає людина.
- **Verification:** Агент прогнано на кількох прикладах PR; зауваження звірені вручну.
- **Result:** Отримано повторюваний структурований review із конкретними рядками.
- **Links:** https://github.com/example/workflow-kit
- **Limitations:** Агент не замінює human approval, а лише готує зауваження.