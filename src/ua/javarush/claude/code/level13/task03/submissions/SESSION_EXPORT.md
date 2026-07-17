# Session Export — Level 13 Task 03

> **Дата:** 2026-07-17
> **Директорія:** `src/ua/javarush/claude/code/level13/task03/`
> **Гілка:** `main` (58803d7 l13 t02)

---

## Дії в сесії

1. **`/clear`** — скинуто контекст сесії
2. **`/context`** — верифіковано обсяг контексту (3.6k / 200k tokens — 2%)
3. **Пошук структури** — прочитано `inputs/scenario.md` та обидва файли в `submissions/`
4. **Аналіз** — три випадки (A, B, C) оцінено за трьома вимірами (Value, Frequency, Controllability)
5. **Запис рішення** — створено `submissions/MCP_DECISION.md`
6. **Експорт сесії** — поточний файл `submissions/SESSION_EXPORT.md`

---

## Вхідні дані

**Сценарій:** три випадки зовнішнього доступу в Commerce OS:

| Випадок | Опис |
|---------|------|
| **A** | Одноразовий локальний stack trace після невдалого тесту |
| **B** | Щоденний intake завдань із issue tracker (багато разів на день) |
| **C** | Автоматичне закриття issue після merge PR |

**Вимоги задачі:**
- Не виходити за рамки `level13/task03`
- Оцінити за трьома вимірами: `Value`, `Frequency`, `Controllability`
- Підсумковий вибір: `manual | read-only MCP | write-capable MCP`
- Для B — рішення має бути `read-only MCP`
- Для A — рішення має бути `manual`
- Для C — рішення не має бути `read-only MCP`

---

## Прийняті рішення

| Випадок | Value | Frequency | Controllability | Decision |
|---------|-------|-----------|-----------------|----------|
| A — одноразовий stack trace | Low | Low | High | **manual** |
| B — щоденний intake issue | Medium | High | Medium | **read-only MCP** |
| C — автоматичне закриття issue | High | Medium | Low | **write-capable MCP** |

### Обґрунтування

**A (manual):** Дані вже на екрані, проєкт відкрито поруч, лог перед очима.
MCP-запит до зовнішнього інструмента не пришвидшить аналіз. Вартість налаштування
MCP-сервера не окупається за одну дію.

**B (read-only MCP):** Висока частота (багато разів на день) окупає налаштування
read-only MCP. Розробник отримує чистий JSON issue без ручного копіювання.
Дія лише читає — немає ризику випадково змінити статус.

**C (write-capable MCP):** Усуває ручну операцію, яку легко забути після merge.
Дія потребує write — закриття issue змінює зовнішній стан системи. Помилка має
наслідки для команди. Write-capable MCP з явним дозволом дає автоматизацію без
втрати контролю.

---

## Створені файли

- `submissions/MCP_DECISION.md` — повний запис рішення з таблицями та обґрунтуваннями
- `submissions/SESSION_EXPORT.md` — поточний файл, trace сесії

---

## Стан репозиторію

```
58803d7 l13 t02
fd6543a l13 t01
4ffd2f0 l12 t10
1c58254 l12 t09
6a0db75 l12 t09
```

Зміни в `task03/` — нові файли (`submissions/MCP_DECISION.md`, `submissions/SESSION_EXPORT.md`).
Незакомічені зміни відсутні за межами `task03/`, окрім `task02/mcp/issue-tracker-draft.json`.