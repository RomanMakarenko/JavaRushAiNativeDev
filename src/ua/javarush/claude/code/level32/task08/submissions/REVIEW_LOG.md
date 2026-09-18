# Журнал перевірки

> Складено у свіжому контексті після `/clear`.
> Джерела: `inputs/mentor-feedback.md`, `README.md`, `SPEC.md`, `EVIDENCE.md`.

## Результат роботи
- severity: must — Кінцева точка `POST /tickets` повертає `500` при порожньому `message`.
  Джерело: `inputs/mentor-feedback.md` (Working result); суперечить критеріям приймання в `SPEC.md`.

## Докази
- severity: should — У `EVIDENCE.md` немає доданого виводу `pytest -q`, проходження тестів не підтверджено.
  Джерело: `inputs/mentor-feedback.md` (Evidence), `EVIDENCE.md` (розділ «Чого не вистачає»).

## Перевірки
- severity: should — Немає зафіксованого результату перевірки порожнього введення як окремої перевірки.
  Джерело: `EVIDENCE.md` (відсутнє підтвердження обробки порожнього `message`).

## Дотримання обсягу
- статус: ok — Реалізація тримається в межах `SPEC.md`: тільки `/tickets` і `/health`, без auth і email.
  Джерело: `SPEC.md` (Scope, Non-goals).

## Документація
- severity: should — `README.md` не уточнює, який порт відкривати після старту (плутанина 8080 / 8085).
  Джерело: `inputs/mentor-feedback.md` (Documentation), `README.md`.

## Зрілість AI-процесу
- статус: ok — SPEC, EVIDENCE і feedback оформлено як окремі артефакти, ланцюжок простежується.
  Джерело: наявність `SPEC.md` і `EVIDENCE.md` у submission package.

## Межа довіри
- severity: must — Відтворюваність зламана: `docker-compose.yml` чекає `APP_HOST_PORT`, а `.env.example` дає `HOST_PORT`, clean start падає.
  Джерело: `inputs/mentor-feedback.md` (Reproducibility).

## Пріоритети покращення
- Сильний сигнал: `SPEC.md` фіксує перевірні критерії приймання — це надійна опора для review.
  Джерело: `inputs/mentor-feedback.md` (Strong signals), `SPEC.md`.
- Пріоритет 1 (must): полагодити відтворюваність і `500` на порожньому `message`.
- Пріоритет 2 (should): додати вивід тестів і уточнити порт у README.