# Session Export — Level 13, Task 05

**Date:** 2026-07-17  
**Project:** `javarush-project`  
**Module:** `ua.javarush.claude.code.level13`  
**Task:** Побудова матриці можливостей MCP-серверів  

---

## Мета

Прочитати маніфести MCP-серверів з `manifests/`, проаналізувати їхні інструменти за категоріями й режимами доступу (read-only vs read-write) та зібрати зведену матрицю можливостей у `submissions/CAPABILITY_MATRIX.md`.

---

## Опрацьовані маніфести

| Файл | Сервер | Категорія | Режим |
|------|--------|-----------|-------|
| `manifests/docs-lookup.md` | `docs-lookup` | docs-lookup | read-only |
| `manifests/issue-tracker.md` | `issue-tracker` | issue-tracker | read-write |
| `manifests/monitoring.md` | `monitoring` | monitoring | read-write |
| `manifests/pr-review.md` | `pr-review` | source-control | read-write |

---

## Виявлені інструменти

### docs-lookup (read-only)
- `search_docs` — 👁️ пошук у документації
- `get_doc_page` — 👁️ отримання сторінки документації

### issue-tracker (read-write)
- `get_issue` — 👁️ отримати картку issue
- `list_issues` — 👁️ список issues за фільтром
- `create_issue` — ✏️ створити нову картку
- `update_issue` — ✏️ змінити поля існуючої картки

### monitoring (read-write)
- `get_metric` — 👁️ отримати значення метрики
- `list_alerts` — 👁️ список активних алертів
- `ack_alert` — ✏️ підтвердити алерт

### pr-review (read-write)
- `get_pull_request` — 👁️ метадані PR
- `get_diff` — 👁️ diff змін PR
- `post_comment` — ✏️ залишити коментар
- `merge_pull_request` — ✏️ виконати merge

---

## Зведена статистика

| | 👁️ Read-only | ✏️ Write | Усього |
|---|---|---|---|
| docs-lookup | 2 | 0 | 2 |
| issue-tracker | 2 | 2 | 4 |
| monitoring | 2 | 1 | 3 |
| pr-review | 2 | 2 | 4 |
| **Разом** | **8** | **5** | **13** |

---

## Експортовані артефакти

- `submissions/CAPABILITY_MATRIX.md` — повна матриця можливостей із групуванням за категоріями, таблицями інструментів і варіантами використання (read-only vs write).
- `submissions/SESSION_EXPORT.md` — цей файл, експорт сесії.

---

## Примітки

- `docs-lookup` — єдиний сервер у суто read-only режимі; write-операції відсутні принципово.
- `monitoring` має лише один write-інструмент (`ack_alert`); створення або вимкнення алертів не передбачено.
- `issue-tracker` і `pr-review` симетричні: по 2 read-only та 2 write інструменти.
- Категорії перекладені українською в матриці: Документація, Трекінг завдань, Моніторинг, Контроль версій.