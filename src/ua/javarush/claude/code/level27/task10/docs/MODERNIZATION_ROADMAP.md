# Roadmap модернізації модуля MRR

> Цей документ описує поступову модернізацію legacy-модуля розрахунку Monthly Recurring Revenue. План призначений для малих, оборотних кроків із постійним порівнянням із поточною поведінкою. Він не є планом повного переписування.

## 1. Межі та правила роботи

- **Scope:** внутрішня реалізація `com.acme.cashflow.mrr`, насамперед `PlanResolver.resolvePlan()` і згодом окремі внутрішні частини `calculate()`.
- **Публічний контракт:** `MrrSummaryResponse.monthlyRecurringRevenue` та інші поля `MrrSummaryResponse` залишаються без змін.
- **Одна зміна за раз:** кожна фаза має один логічний slice, окремий validation checkpoint і зрозумілий rollback.
- **Поведінкова сумісність:** legacy-правила, зокрема вибір останнього підхожого плану при overlapping-підписках, зберігаються, доки окремо не буде погоджено інше бізнес-правило.
- **Default-safe:** новий шлях не вмикається за замовчуванням; наявний feature flag `cashflow.mrr.v2-single-plan` має безпечне значення `false`.
- **Зміни тільки після gate:** наступний slice не починається, якщо safety baseline або критерії поточної фази не пройдені.

## 2. Поточний стан

- MRR — фінансово критичний модуль: результат потрапляє у звіти.
- `MrrCalculator.calculate()` — великий legacy-метод із гілкуванням за типами плану та proration; зміна поведінки має високий ризик.
- `PlanResolver.resolvePlan()` — локальний seam із дублювальною lookup-логікою; при кількох активних планах навмисно повертає **останній** підхожий план.
- `MrrSummaryResponse` є зовнішнім контрактом для дашборда та інтеграцій; поле `monthlyRecurringRevenue` вважається стабільним.
- Characterization baseline уже описаний у `docs/CHARACTERIZATION_TESTS.md`:
  - повний місяць;
  - річний план, приведений до місяця;
  - legacy-округлення refund;
  - вибір активного плану на дату.
- Відомі прогалини baseline: overlapping subscriptions і зміна плану в середині місяця не покриті окремими characterization-сценаріями.
- Поточна перевірка baseline: `./gradlew test --tests "*Characterization*"`.

## 3. Цільові результати

Досягнення roadmap означає не «нову архітектуру за один реліз», а контрольований стан, у якому:

1. внутрішні seams модуля можна змінювати малими незалежними slices;
2. кожен slice має зафіксований baseline, перевірку еквівалентності та план rollback;
3. для підтриманих сценаріїв результати до і після зміни ідентичні, включно з грошовим scale/rounding та legacy quirks;
4. зовнішній API і формат даних споживачів не змінюються;
5. rollout нового шляху контролюється feature flag і може бути зупинений без міграції даних;
6. невідомі сценарії явно позначені, а не замасковані припущеннями.

## 4. Safety baseline перед змінами

Перед кожним implementation slice команда Billing Platform фіксує:

- зелений прогін `./gradlew test --tests "*Characterization*"`;
- повний результат `./gradlew test` як regression signal;
- поточний `git diff` і перелік файлів, дозволених для slice;
- очікувану поведінку на representative inputs: активний план, відсутній план, межі дат, overlapping-плани, повний/річний план і refund rounding;
- для кожного порівняння — exact equality для `monthlyRecurringRevenue`, `activeSubscriptions` і вибраного plan id там, де значення доступне;
- список відомих unknowns та рішення, чи вони блокують наступну фазу.

Baseline не змінює код і не виправляє legacy-поведінку. Якщо characterization-тест виявляє існуючий quirk, його спочатку фіксують як сумісну поведінку, а не виправляють у межах рефакторингу.

## 5. Phased план

### Phase 0 — зафіксувати baseline і межі

**Мета:** зробити поточну поведінку перевірюваною до першого коду.

**Роботи:**

- підтвердити characterization і full regression прогін;
- уточнити representative input set для `PlanResolver`;
- зафіксувати public API, schema та список non-goals;
- підготувати окремий change record із owner, commit і результатами перевірок.

**Validation checkpoint:** baseline зелений; немає непояснених падінь; `MrrSummaryResponse` і схеми даних не мають diff.

**Success criteria:**

- `*Characterization*` — 100% green;
- `./gradlew test` — green;
- 0 змінених production-файлів у цій фазі;
- усі known gaps із секції 2 явно перелічені.

**Rollback:** runtime rollback не потрібен, бо поведінкових змін немає. Якщо baseline-артефакт некоректний, скасувати лише запис фази та повторити його; не переходити до Phase 1.

### Phase 1 — один pilot slice: seam навколо `PlanResolver`

**Мета:** перевірити процес на найменшому локальному seam, не змінюючи результат.

**Єдиний pilot slice:** виділити lookup seam (`PlanLookup`) навколо `resolvePlan()` і переключити виклик на нього без зміни алгоритму. Порядок планів, inclusive/exclusive межі дат, `null` для відсутнього плану та quirk «останній підхожий» мають залишитися такими самими.

**Обмеження pilot:**

- не чіпати `MrrSummaryResponse` або інші public API;
- не змінювати `MrrCalculator.calculate()`;
- не додавати бізнес-правила для overlapping subscriptions;
- не робити schema/data migration;
- feature flag `cashflow.mrr.v2-single-plan` лишається вимкненим за замовчуванням;
- slice має бути малим і локальним: лише файли seam, wiring і необхідні перевірки.

**Validation checkpoints:**

1. compile та unit/characterization checks;
2. порівняння old/new шляху на representative input set;
3. перевірка overlapping, меж дат і `null` result;
4. review diff на відсутність API/schema/business-rule змін;
5. окреме рішення owner: proceed або rollback.

**Success criteria:**

- 100% зелені characterization і full regression тести;
- 0 розбіжностей output між legacy та pilot шляхом на всіх agreed inputs;
- 0 змін у public API та схемі;
- 0 P1/P2 regression incidents під час контрольованого rollout;
- feature flag дозволяє повернутися на legacy шлях без redeploy або міграції даних, якщо це підтримує наявний runtime;
- diff обмежений одним логічним slice, без unrelated cleanup.

**Rollback:** негайно встановити `cashflow.mrr.v2-single-plan=false` і повернути виклики на legacy шлях; якщо проблема відтворюється — revert commit pilot slice. Baseline та попередні slices не змінювати. Дані, schema й public API не потребують rollback.

### Phase 2 — послідовні внутрішні slices після pilot

**Мета:** поширити перевірений підхід на наступну внутрішню ділянку без зміни зовнішньої поведінки.

**Порядок:**

1. спочатку закрити або прийняти як explicit risk unknowns щодо overlapping і mid-month plan change;
2. обрати один локальний seam у `calculate()`;
3. виконати один extraction за раз, не поєднуючи його з rounding, proration чи зміною API;
4. для кожного slice повторити baseline → equivalence check → review → gated rollout.

Після кожного slice оновлюються тільки технічні записи: scope зміни, результати перевірок, відомі ризики та rollback. Legacy rounding і proration не «покращуються» неявно.

**Validation checkpoints:** characterization, full regression, differential comparison на погоджених сценаріях, перевірка latency/error/financial-delta сигналів у доступному середовищі та ручне sign-off owner.

**Success criteria:**

- кожен slice проходить усі gates без винятків;
- 0 unexplained output mismatches;
- 0 public API/schema changes;
- жоден slice не охоплює одночасно кілька незалежних поведінкових змін;
- rollback попереднього slice перевірений до старту наступного.

**Rollback:** зупинити rollout на останньому невдалому slice, вимкнути його flag (якщо застосовується) і revert лише останньої зміни. Повернутися до останнього green commit; не відкотити весь roadmap і не переписувати історію baseline.

### Phase 3 — стабілізація та закриття окремих slices

**Мета:** залишити тільки доведені, підтримувані внутрішні seams і не створювати незавершену міграцію.

**Роботи:**

- підтвердити стабільність після погодженого періоду контрольованого rollout;
- перевірити, що legacy fallback і rollback інструкції реально доступні операційній команді;
- документувати залишені unknowns та свідомо відкладені ділянки;
- прибирати flag або legacy path лише окремим погодженим change request після доказу, що rollback більше не потрібен.

**Validation checkpoint:** фінальний regression прогін, differential evidence для всіх модернізованих slices, sign-off Billing Platform і підтвердження відсутності API/schema drift.

**Success criteria:**

- усі прийняті slices мають owner, evidence і rollback note;
- 100% обов'язкових baseline/regression gates зелені на фінальному commit;
- немає відкритих unexplained financial deltas;
- не залишено частково увімкненого шляху без documented fallback.

**Rollback:** до останнього затвердженого green commit; увімкнути legacy path і вимкнути новий flag. Якщо flag уже видалений окремим change request, rollback виконується revert цього change request, без зміни даних і схеми.

## 6. Вимірювані критерії успіху roadmap

Roadmap вважається успішним, якщо для кожного прийнятого slice одночасно виконуються такі критерії:

| Область | Критерій | Evidence |
|---|---|---|
| Регресія | `./gradlew test --tests "*Characterization*"` і `./gradlew test` — green | CI/local run output |
| Поведінка | 0 unexplained mismatches old/new на погодженому наборі inputs | differential comparison record |
| Гроші | `monthlyRecurringRevenue` збігається точно, включно з scale та rounding | comparison report |
| Контракт | 0 змін у public API та schema | API/schema diff review |
| Rollout | legacy fallback доступний і перевірений | flag/rollback evidence |
| Scope | один логічний slice, без unrelated cleanup | reviewed diff |
| Інциденти | 0 P1/P2 regression incidents, пов'язаних зі slice, за період контрольованого rollout | incident/monitoring record |
| Документація | owner, checkpoint, evidence і rollback записані | change record / refactor log |

Якщо вимір недоступний у конкретному середовищі, це фіксується як blocker або explicit unknown, а не замінюється припущенням про успіх.

## 7. Non-goals

У межах цієї roadmap **не плануються і не обіцяються**:

- upgrade framework або runtime;
- зміни схеми даних, persistence format чи міграції;
- додавання або зміна business rules;
- зміна proration, refund rounding чи overlapping semantics «для виправлення» legacy-поведінки;
- зміна `MrrSummaryResponse` або будь-якого іншого public API;
- одночасне переписування всього `MrrCalculator`;
- full rewrite, big-bang migration або гарантія повної заміни legacy-коду;
- видалення feature flag до окремого доказового gate;
- unrelated cleanup, масове перейменування чи оптимізація без вимірюваного ризик-контролю.

Будь-яка потреба поза цим списком оформлюється окремою ініціативою з новим scope, baseline та погодженими критеріями rollback.