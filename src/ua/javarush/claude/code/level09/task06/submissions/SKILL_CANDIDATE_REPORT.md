# Skill Candidate Report

## issue-analysis-1.md → Promote to skill

- **Trigger:** У трекер падає новий bug report.
- **Stable inputs:** Текст issue, посилання на зачеплені файли.
- **Expected output:** `TASK_SPEC.md` — інженерна специфікація з розділами Goal, Scope, Acceptance criteria, Verification.
- **Constraints:** Реалізацію не починати, лише специфікація.

**Evidence:**
1. Сценарій повторюється в `prompts/issue-analysis-1.md`, `prompts/issue-analysis-2.md` і `prompts/issue-analysis-3.md` — три окремі файли описують той самий workflow: bug report → TASK_SPEC.md.
2. Усі три варіанти мають ідентичну структуру виходу (Goal, Scope, Acceptance criteria, Verification), що вказує на сталий, багаторазово застосовуваний процес.
3. README.md закріплює TASK_SPEC.md як очікуваний артефакт для кожного вхідного bug report, підтверджуючи, що це стандартний, а не одноразовий сценарій.

---

## issue-analysis-2.md → Promote to skill (об'єднати з issue-analysis-1)

- **Trigger:** У трекер падає новий bug report.
- **Stable inputs:** Текст issue, логи або повідомлення про помилку.
- **Expected output:** `TASK_SPEC.md` — інженерна специфікація з розділами Goal, Scope, Acceptance criteria, Verification.
- **Constraints:** Реалізацію не починати, лише специфікація.

**Evidence:**
1. Той самий workflow, що й у `prompts/issue-analysis-1.md` — обидва перетворюють bug report на TASK_SPEC.md з тими самими чотирма розділами.
2. У `prompts/` є третій варіант (`issue-analysis-3.md`) з тією ж метою, що підтверджує: команда постійно стикається з цим завданням і накопичила копії замість одного джерела.

---

## issue-analysis-3.md → Promote to skill (об'єднати з issue-analysis-1)

- **Trigger:** У трекер падає новий bug report.
- **Stable inputs:** Текст issue, скріншот або опис кроків відтворення.
- **Expected output:** `TASK_SPEC.md` — інженерна специфікація з розділами Goal, Scope, Acceptance criteria, Verification.
- **Constraints:** Реалізацію не починати, лише специфікація.

**Evidence:**
1. Ідентичний вихід (TASK_SPEC.md з Goal, Scope, Acceptance criteria, Verification) з двома іншими файлами в `prompts/` — три копії того самого сценарію.
2. README.md описує цей workflow як стандартну процедуру, а не одноразове завдання, тому потрібен єдиний skill замість трьох розрізнених prompt-файлів.

---

## one-off-idea.md → Keep as prompt

- **Trigger:** Потрібно згенерувати ASCII-банер для README для скриншота до посту.
- **Stable inputs:** Назва проєкту.
- **Expected output:** ASCII-заголовок, вставлений на початок README.
- **Constraints:** Лише для скриншота, не для продакшну.

**Evidence:**
1. Описаний як «разова нотатка для експерименту» та «до повторюваного workflow стосунку не має, другий раз навряд чи знадобиться» — тобто свідомо одноразовий.
2. Сценарій не має жодних дублікатів або варіацій у `prompts/`, що підтверджує його унікальність і нестандартність.