# landing-audit-web

Невеликий вебінструмент для аудиту лендінгів. Приймає один URL, проганяє набір
евристичних перевірок (заголовок, meta description, наявність H1, alt у зображень) і
повертає короткий звіт зі списком зауважень.

## Локальний запуск

```bash
npm install
npm run build
npm test
```

## Команди

- `npm run lint` — статична перевірка `src/`
- `npm run build` — збірка статичного бандла в `dist/`
- `npm test` — unit-тести евристик аудиту
- `npm run smoke` — короткий smoke-прогін аудиту на sample URL

## Структура

- `src/` — код евристик і рендер звіту
- `scripts/` — build- і smoke-скрипти
- `test/` — unit-тести
- `docs/` — нотатки до demo