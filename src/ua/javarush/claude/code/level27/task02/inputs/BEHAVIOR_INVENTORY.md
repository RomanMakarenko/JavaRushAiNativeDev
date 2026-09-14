# Behavior inventory — модуль mrr-engine

Модуль `mrr-engine` обчислює Monthly Recurring Revenue (MRR) за підписками CashFlow Dashboard.
Документ описує фактичну спостережувану поведінку на сьогодні, без оцінки «правильно/неправильно».

## Flow 1 — refund у поточному місяці

- Тригер: повернення (refund) оформлено в поточному розрахунковому місяці.
- Спостережувана поведінка: refund у поточному місяці поки впливає на MRR наступного місяця
  (carry-forward), а не зменшує MRR поточного.
- Observable outputs: `mrr[current]`, `mrr[next]`.
- Це відомий legacy-quirk, поведінка небажана, але використовується downstream-звітами.

## Flow 2 — plan switch (upgrade/downgrade)

- Тригер: підписник змінює тариф у межах місяця (plan switch).
- Спостережувана поведінка: MRR перераховується пропорційно (prorated) від дати зміни.
- Observable outputs: `mrr[current]`, рядок `planId` у snapshot.

## Flow 3 — pause / resume в одному місяці

- Тригер: підписку ставлять на паузу (pause) і знімають із паузи (resume) в межах місяця.
- Спостережувана поведінка: при pause і resume в одному місяці MRR місяця не обнуляється,
  нарахування зберігається як за повний місяць.
- Observable outputs: `mrr[current]`.

## Flow 4 — генерація місячного snapshot

- Тригер: формування місячного MRR-звіту.
- Спостережувана поведінка: у snapshot потрапляють нестабільні поля `generatedAt` і `traceId`,
  порядок планів не гарантований.
- Observable outputs: JSON snapshot звіту.