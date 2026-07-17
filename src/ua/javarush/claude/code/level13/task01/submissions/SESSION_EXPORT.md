# Session Export — Level 13, Task 01

**Date:** 2026-07-17  
**Model:** claude-course-fast  
**Context window:** 3.5k / 200k tokens (2%)  
**Branch:** `main` (`fd6543a13`)  
**Working directory:** `level13/task01/`

---

## Overview

Session присвячена аналізу меж контексту проєкту: розмежуванню внутрішніх артефактів codebase та зовнішніх даних, що надходять через MCP-поверхні.

---

## Files examined

| # | File | Nature |
|---|------|--------|
| 1 | `docs/CODEBASE_INVENTORY.md` | Внутрішній — опис модулів Commerce OS |
| 2 | `src/main/java/com/acme/orders/OrderController.java` | Внутрішній — REST-контролер замовлень |
| 3 | `external/issue-COM-481.md` | Зовнішній — задача з issue tracker (подвійне списання) |
| 4 | `external/monitoring-alert.txt` | Зовнішній — алерт моніторингу (дублікати замовлень 3.8%) |
| 5 | `submissions/MCP_SURFACES.md` | Внутрішній — артефакт аналізу (створено в цій сесії) |
| 6 | `submissions/SESSION_EXPORT.md` | Внутрішній — цей файл |

## Artifacts created

### `submissions/MCP_SURFACES.md` (81 рядків)

Документ, що:

1. **Інвентаризує** всі файли проєкту з поділом на внутрішні (codebase) та зовнішні (MCP-поверхні), із зазначенням джерела та життєвого циклу кожного.

2. **Обґрунтовує**, чому MCP додає зовнішній доступ, а не замінює репозиторій:
   - **Різна природа даних:** репозиторій — версіонований *curated snapshot* (статичний); MCP — неверсіонований *live stream* (динамічний).
   - **Різні цілі:** код фіксує архітектуру та бізнес-логіку; MCP несе інциденти, задачі та метрики в реальному часі.
   - **MCP — міст, а не заміна:** репозиторій є authoritative source; MCP не має гарантій узгодженості, тимчасовий, і до нього застосовується принцип найменших привілеїв.
   - **Висновок:** MCP розширює контекст назовні (як система працює насправді), а не дублює контекст усередину (як система повинна працювати).

### `submissions/SESSION_EXPORT.md` (цей файл)

Експорт сліду сесії.

## Context classification

### Internal (codebase)
Verisoned, reviewed, authoritative:

```
docs/CODEBASE_INVENTORY.md
src/main/java/com/acme/orders/OrderController.java
submissions/MCP_SURFACES.md
submissions/SESSION_EXPORT.md
```

### External (MCP surfaces)
Unversioned, streamed, operational:

```
external/issue-COM-481.md     ← commerce-issue-tracker
external/monitoring-alert.txt ← commerce-monitoring
```

## Key observations

- **COM-481** (подвійне списання) описує проблему ідемпотентності: відсутність захисту від повторного submit на рівні `POST /api/orders`.
- **Monitoring alert** підтверджує проблему метрикою: `orders.duplicate_rate = 3.8%` при порозі `1.0%`. Алерт корелює з COM-481.
- **OrderController** — тонкий шар, що делегує в `OrderService`. Сама логіка запобігання дублікатам (якщо вона є) знаходиться в сервісі, який не входить у поточний task01.
- **MCP_SURFACES.md** не містить коду або технічного рішення — це аналітичний артефакт, що фіксує розуміння меж контексту.

## Git state

```
fd6543a l13 t01
```

Робоча директорія чиста, змін немає.