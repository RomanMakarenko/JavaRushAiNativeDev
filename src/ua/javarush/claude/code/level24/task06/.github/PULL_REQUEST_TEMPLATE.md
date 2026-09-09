# Опис змін

Коротко: що і навіщо змінюється.

## Чек-лист перед review

- [ ] Локальні quality gates пройдено (`docs/QUALITY_GATES.md`)
- [ ] Немає прямого деплою (`deploy`) з цієї гілки
- [ ] Немає секретів (`.env`, токени) у diff
- [ ] Якщо зачеплено `db/migration` — вказано план відкоту
- [ ] Немає force push у protected branch

## Ризик зміни

Див. `docs/RISK_CLASSIFICATION.md`. Укажи рівень: low / medium / high.