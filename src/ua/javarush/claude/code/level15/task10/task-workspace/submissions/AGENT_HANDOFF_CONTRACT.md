# AGENT HANDOFF CONTRACT — backend-owner

> Контракт для ролі **backend-owner** у пайплайні **REFUND-204**.

---

## Input artifacts

| Джерело | Що містить |
|---------|------------|
| `inputs/issue.md` | REFUND-204: подвійне списання при повторному refund через `RefundService.refund(chargeId, amount)` |
| `inputs/artifacts-note.md` | Порядок інтеграції ролей, вимоги до вихідних артефактів |
| `inputs/allowed-paths.txt` | Перелік файлів, які ця роль може змінювати |
| `inputs/forbidden-paths.txt` | Перелік файлів і зон, які ця роль не чіпає |

---

## Merge order position

**1 / 3**

Backend-owner інтегрується першим. Його вихід — вхід для:
- `frontend-owner` (знає, на який ідемпотентний механізм уже покладатися)
- `tests-owner` (будує regression-тести поверх готового backend + frontend)

---

## Allowed scope

Спирається на `inputs/allowed-paths.txt`.

| Файл | Що робити |
|------|-----------|
| `src/api/orders/RefundService.java` | Додати перевірку існуючого refund (`findByChargeId` перед створенням) |
| `src/api/orders/RefundRepository.java` | Додати метод `findByChargeId(chargeId)` (якщо відсутній) |

### Контекст проблеми

`RefundService.refund(chargeId, amount)` не перевіряє, чи вже існує refund для цього `chargeId`.
Повторний виклик створює другий платіж — клієнту повертається сума двічі.

**Бажана поведінка:** повторний refund по тому самому `chargeId` не створює другий рефанд.

### Рішення — ідемпотентність

Додати перевірку в `RefundService.refund()` перед створенням нового refund:
- Використати `RefundRepository` для пошуку існуючого refund за `chargeId`.
- Якщо refund вже існує — повернути його (або відповідний status-код), не створюючи новий.
- Якщо не існує — створити новий refund у звичайному порядку.

---

## Forbidden paths

Спирається на `inputs/forbidden-paths.txt`.

| Шлях | Причина |
|------|---------|
| `src/web/checkout/**` | Зона frontend-owner (блокування повторного кліку) |
| `src/api/orders/dto/RefundRequest.java` | Спільний DTO — не змінюється в цьому issue |
| `config/**` | Конфігурація середовища — не належить до задачі |

---

## Stop condition

Роль вважає задачу виконаною, коли:

1. **`RefundRepository`** має метод `findByChargeId(String chargeId)` (або еквівалент).
2. **`RefundService.refund()`** викликає цей метод перед створенням нового refund і не створює дублікат.
3. **Тести зони проходять** — всі unit-тести, що стосуються `RefundService` та `RefundRepository`.
4. **Жоден файл із `forbidden-paths.txt` не змінено.**

---

## Output artifacts

Після завершення роботи роль **обов'язково** передає далі:

| Артефакт | Формат | Приклад |
|----------|--------|---------|
| **diff** | Git diff або patch-файл | Зміни в `RefundService.java` + `RefundRepository.java` |
| **лог тестів** | stdout / текстовий файл | `BUILD SUCCESSFUL` або `Tests run: 12, Failures: 0` |
| **резюме** | Один рядок | `backend: додано ідемпотентність refund за chargeId` |

Резюме для наступних ролей:

```
backend: додано ідемпотентність refund за chargeId — RefundRepository.findByChargeId +
перевірка в RefundService.refund() перед створенням нового refund
```

---

## Evidence

Усі нижчеперелічені артефакти додаються до `submissions/` після завершення роботи.

| № | Артефакт | Призначення |
|---|----------|-------------|
| 1 | **Git diff** (або patch-файл) | Фіксує зміни в `RefundService.java` та `RefundRepository.java` |
| 2 | **Test output** | stdout або log-файл із результатом прогону unit-тестів зони |
| 3 | **File refs** | Посилання на змінені файли у вигляді `src/api/orders/RefundService.java:+N` |

Evidence підтверджує, що роль виконала Stop condition і передає downstream-ролям (frontend-owner, tests-owner) конкретний стан коду.

---

## Типова послідовність роботи

```
1. Ознайомитися з inputs/issue.md, inputs/allowed-paths.txt, inputs/forbidden-paths.txt
2. Перевірити RefundRepository — чи є findByChargeId (додати, якщо ні)
3. Оновити RefundService.refund() — додати idempotency check
4. Запустити тести зони → підтвердити проходження
5. Згенерувати diff (git diff або patch)
6. Записати резюме
7. Покласти артефакти в submissions/
```

---

*Contract version 1.1. Pipeline REFUND-204, backend-owner role.*