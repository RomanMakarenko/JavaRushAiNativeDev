# Rubric self-check

Дата перевірки: 2026-09-18  
Об’єкт перевірки: поточна директорія `level32/task05` та доступні в ній артефакти.

> Статуси оцінюють лише те, що можна підтвердити доступними файлами. У поточній директорії на момент перевірки є `inputs/grading-rubric.md` і цей файл; `README.md`, `SPEC.md`, `DEMO.md` та `EVIDENCE.md` не знайдені. Тому відсутність evidence не замінюється припущеннями.

## Результат

| Напрям | Статус | Evidence | Короткий коментар |
|---|---|---|---|
| working result | `weak` | `inputs/grading-rubric.md:9-12`; аудит файлів поточної директорії: немає `DEMO.md`, відео або smoke-check | Core flow і його фактичний результат у доступних матеріалах не продемонстровані. |
| demo clarity | `weak` | `inputs/grading-rubric.md:13-15`; аудит файлів: немає `DEMO.md`, screenshots або walkthrough | Цінність і сценарій демонстрації неможливо швидко зрозуміти без demo-артефакту. |
| portfolio readiness | `weak` | `inputs/grading-rubric.md:17-19`; аудит файлів: немає `README.md` або опису оформлення репозиторію | Proof-of-work для зовнішнього reviewer наразі не підтверджений. |

## Процес

| Напрям | Статус | Evidence | Короткий коментар |
|---|---|---|---|
| scope discipline | `weak` | `inputs/grading-rubric.md:23-25`; аудит файлів: немає `SPEC.md` з non-goals | Межі scope і свідомі відмови від функцій не зафіксовані в доступному evidence. |
| task spec quality | `weak` | `inputs/grading-rubric.md:27-29`; аудит файлів: немає `SPEC.md` з acceptance criteria | Постановка задачі та критерії приймання не представлені окремим артефактом. |
| Git/diff discipline | `weak` | `inputs/grading-rubric.md:31-33`; доступна директорія не містить walkthrough історії commits або diff | Акуратність змін не можна оцінити лише за наявним rubric-файлом. |
| traceability | `weak` | `inputs/grading-rubric.md:35-37`; аудит файлів: немає `EVIDENCE.md` з ланцюжком issue → commit → PR | Зв’язок між вимогою, зміною та результатом не простежується. |

## Якість

| Напрям | Статус | Evidence | Короткий коментар |
|---|---|---|---|
| tests/checks | `weak` | `inputs/grading-rubric.md:41-43`; аудит файлів: немає тестів, smoke-check або verification notes | Machine-checkable доказ коректності в доступних матеріалах відсутній. |
| documentation | `weak` | `inputs/grading-rubric.md:45-47`; аудит файлів: немає `README.md` і setup-інструкції | Зовнішній користувач не має підтвердженого способу зрозуміти або запустити проєкт. |
| risk awareness | `weak` | `inputs/grading-rubric.md:49-51`; аудит файлів: немає README з розділом limitations | Межі застосування, ризики та відомі обмеження не зафіксовані. |

## AI-native workflow

| Напрям | Статус | Evidence | Короткий коментар |
|---|---|---|---|
| Claude Code usage / relevance over quantity | `medium` | `inputs/grading-rubric.md:53-58`; цей self-check: evidence-first перевірка rubric і свідоме обмеження змін одним запитаним файлом | Для цього вузького документа використано релевантний, мінімальний workflow без штучного додавання AI-механізмів; доказів ширшого процесу ще немає. |

## Advanced credits та свідомі відмови

- **Advanced credit — релевантний поточному проєкту:** evidence-first Claude Code workflow: спочатку прочитано rubric, потім перевірено доступні артефакти, після чого створено один цільовий self-check із явними статусами й посиланнями. Це релевантно, бо поточний deliverable є саме контрольним документом, а не реалізацією runtime-функцій.
- **Advanced-механізм, не потрібний поточному формату:** `Agent`/subagents, worktree та parallel orchestration не потрібні для однофайлового read-only розбору rubric; їхнє залучення додало б процесу шуму без нової evidence. Так само відео або screenshots не є доречними для цього документального deliverable без окремого працюючого UI/core flow.

## Top 3 gaps before defense

1. **Додати базові проєктні артефакти:** `README.md`, `SPEC.md`, `DEMO.md` та `EVIDENCE.md` з узгодженими посиланнями.
2. **Підтвердити working result машинно:** додати runnable demo або smoke-check і коротко зафіксувати очікуваний та фактичний результат.
3. **Побудувати traceability-посилання:** зв’язати issue → commit → PR/diff → verification note та окремо описати non-goals, limitations і acceptance criteria.

## Підсумковий висновок

Готовність до defense наразі **низька**: за 10 із 11 напрямів доступне evidence відсутнє, а AI-native workflow має лише середній статус для цього вузького deliverable. Документ rubric self-check готовий, але перед defense потрібно спочатку створити й перевірити основні runtime, demo, процесні та документаційні артефакти, а потім оновити статуси на основі фактичних доказів.