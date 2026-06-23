Вручну відрецензуйте Claude уже зроблені зміни, перш ніж їх приймати.

У робочому дереві ShopFlow є diff по bugfix у newsletter preferences. Приймати зміни навмання не можна — спочатку потрібен короткий review з фокусом на scope (чи не вийшли за межі) і risks. Це підхід review-first: дивимося diff і звіряємо його з очікуваним scope.

Що потрібно зробити (у сесії Claude Code):

    Розгорніть репозиторій: виконайте bash init.sh (або попросіть Claude Code запустити його) — він створює baseline-коміт і кладе незакомічений bugfix у робоче дерево, інакше git diff буде порожнім і рецензувати буде нічого.
    Відкрийте inputs/issue.md і inputs/expected-scope.md.
    Попросіть Claude показати поточні зміни робочого дерева (наприклад git diff) і провести їхній review.
    Попросіть створити submissions/REVIEW_SUMMARY.md із розділами Scope, Risks, Decision, Next step.
    У summary має бути щонайменше один конкретний факт із diff і позначка, чи збігається diff з expected-scope.md. Підсумкове рішення — APPROVE, REQUEST_CHANGES або REJECT.


Production code змінювати не потрібно.

Файли для зміни:

    submissions/REVIEW_SUMMARY.md

Вимоги:

    •
    Зміни робочого дерева мають бути переглянуті через команду показу diff (наприклад `git diff` або актуальний еквівалент із `/help`) і відрецензовані всередині Claude CLI.
    •
    `submissions/REVIEW_SUMMARY.md` повинен містити розділи `Scope`, `Risks`, `Decision`, `Next step`.
    •
    Підсумкове рішення має бути одним із `APPROVE`, `REQUEST_CHANGES`, `REJECT`.
    •
    У документі має бути вказано щонайменше один факт із diff.
    •
    У документі має бути позначено зв'язок між diff і `inputs/expected-scope.md`.
    •
    Вихідні файли проєкту змінювати не потрібно.