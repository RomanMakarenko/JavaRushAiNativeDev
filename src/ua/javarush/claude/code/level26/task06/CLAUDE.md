# CLAUDE.md — CashFlow Dashboard (legacy billing)

Це legacy-сервіс білінгу. Більша частина коду написана давно, документація застаріла.

## Важливі правила
- Вихідні файли застосунку (`src/main/java`) і тести (`src/test/java`) у цій задачі змінювати НЕ потрібно.
- Старий `docs/ARCHITECTURE.md` чіпати НЕ потрібно — це історична довідка.
- Будь-які власні артефакти складай лише в `submissions/`.

## Де що лежить
- `src/main/java/com/rush/billing/mrr` — обчислення MRR (Monthly Recurring Revenue).
- `src/main/java/com/rush/billing/subscription` — керування підписками.
- `src/main/java/com/rush/billing/job` — планувальники (`@Scheduled`).
- `docs/` — застаріла архітектурна документація.