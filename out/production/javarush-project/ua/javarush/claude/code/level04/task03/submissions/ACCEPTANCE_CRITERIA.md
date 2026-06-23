# Acceptance Criteria — `update_display_name` trimming

**Issue:** display name зберігається із зайвими пробілами  
**Модуль:** `app/profile.py`, функція `update_display_name`  
**Дата:** 2026-06-21

---

## AC 1 — Провідні та замикальні пробіли видаляються (trim)

| | |
|---|---|
| **Positive scenario** | `update_display_name("  Alice  ")` повертає `"Alice"` |
| **Negative scenario** | Функція повертає `"  Alice  "` (поточна поведінка — **баг**) |
| **Compatibility** | Сигнатура `(name: str) -> str` не змінюється; логіка всередині змінюється на `name.strip()` |

---

## AC 2 — Ім'я без пробілів зберігається без змін (regression)

| | |
|---|---|
| **Positive scenario** | `update_display_name("Bob")` повертає `"Bob"` |
| **Negative scenario** | Функція обрізає непробільні символи (агресивне чищення) |
| **Compatibility** | Happy path (AC 2) і trim (AC 1) працюють одночасно; жоден не ламає інший |

---

## AC 3 — Рядок з самих пробілів повертає порожній рядок (edge case)

| | |
|---|---|
| **Positive scenario** | `update_display_name("   ")` повертає `""` |
| **Negative scenario** | Функція зберігає `"   "` як валідне ім'я (поточна поведінка — **баг**) |
| **Compatibility** | Функція завжди повертає `str`; порожній рядок — валідний `str` |

---

## AC 4 — Пробіли лише з одного боку також обрізаються

| | |
|---|---|
| **Positive scenario** | `update_display_name("John ")` → `"John"`, `update_display_name("  Jane")` → `"Jane"` |
| **Negative scenario** | Пробіли видаляються тільки з лівого або тільки з правого боку, а не з обох |
| **Compatibility** | Симетричний trim з обох боків; символи всередині (напр. `"A B"`) не зачіпаються |

---

## Файли, що підлягають виправленню

| Файл | Зміна |
|---|---|
| `app/profile.py` (рядок 6) | `saved = name` → `saved = name.strip()` |
| `tests/test_profile.py` | Додати тести на AC 1, AC 3, AC 4; існуючий тест AC 2 залишити без змін |

## Вхідні дані (досліджені файли)

- `inputs/issue.md` — опис баги
- `inputs/evidence-summary.md` — емпіричні спостереження
- `app/profile.py` — функція з багом
- `tests/test_profile.py` — поточний тест (тільки happy path)
