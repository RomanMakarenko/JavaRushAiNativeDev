# Capstone Grading Rubric

Capstone оцінюється не за відчуттями, а за rubric. Усі напрями згруповано
в чотири блоки: result, process, quality та AI-native workflow. Усередині блоків
є 11 напрямів оцінювання. Reviewer читає ті самі артефакти (`README.md`,
`SPEC.md`, `DEMO.md`, `EVIDENCE.md`), що й автор, але очима рецензента.

## Блок 1. Результат

### working result
Чи працює core flow проєкту: чи доведено його до результату, а не лише задумано.
Evidence: `DEMO.md`, video, smoke check.

### demo clarity
Чи можна швидко зрозуміти цінність проєкту за демонстрацією.
Evidence: `DEMO.md`, screenshots, walkthrough.

### portfolio readiness
Чи годиться проєкт як proof-of-work для портфоліо.
Evidence: `README.md`, оформлення репозиторію.

## Блок 2. Процес

### scope discipline
Чи був scope свідомим, а не «воно само розрослося».
Evidence: `SPEC.md`, фіксація non-goals.

### task spec quality
Якість постановки задачі та критеріїв приймання.
Evidence: `SPEC.md`.

### Git/diff discipline
Акуратність commits і diff: малі осмислені зміни.
Evidence: історія commits, diff, PR walkthrough.

### traceability
Прослідковуваність від issue до результату.
Evidence: `EVIDENCE.md`, посилання issue → commit → PR.

## Блок 3. Якість

### tests/checks
Чи є machine-checkable докази коректності.
Evidence: тести, smoke checks, verification notes.

### documentation
Чи зрозумілі README і setup зовнішній людині.
Evidence: `README.md`, setup-інструкція.

### risk awareness
Чи бачить автор межі й ризики, чи чесно описані limitations.
Evidence: розділ limitations у `README.md`.

## Блок 4. AI-native workflow

### Claude Code usage / relevance over quantity
Чи осмислено застосовувався Claude Code, чи пояснено доречність кожного механізму,
а не зібрано «AI-зоопарк» заради кількості.
Evidence: `EVIDENCE.md`, review notes, нотатки про свідомі відмови.