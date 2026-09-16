# План rollback для пілоту Reports Boot 3

## Code rollback

- Пілот працює в окремому worktree. Для відкату зупинити процес пілоту та повернути worktree до базового коміту `a1b9f3c` — коміту, зафіксованого в `inputs/BACKUP_MANIFEST.md`.
- Перевірити `git diff a1b9f3c` у worktree; після відкату diff має бути порожнім. Production-код не змінюється, оскільки production deploy не входить до пілоту.
- Власник дії: Анна Котова (модуль `reports`).

## Dependency rollback

- Зупинити збірки та відновити `build.gradle.kts` з `backups/build.gradle.kts.pre`.
- Відновити `gradle.lockfile` з `backups/gradle.lockfile.pre`, очистити кеш артефактів пілоту й повторити dependency resolution з відновленим lockfile.
- Перевірити, що збірка проходить на відновлених файлах і не підтягує версії, додані для Boot 3. Власник дії: Ігор Лебедєв.

## Config rollback

- Негайно встановити `reports_engine.boot3_pilot_enabled=false` у `application-pilot.yml`; це швидкий feature-flag switch, який повертає старий шлях `reports` без зміни коду.
- За потреби повного відновлення конфігурації замінити `config/application-pilot.yml` копією `backups/application-pilot.yml.pre`.
- Перевірити effective config після перезапуску: flag має дорівнювати `false`, а маршрути читання звітів — старому шляху. Власник дії: Ігор Лебедєв.

## Data rollback

- Пілот має режим read-only: INSERT, UPDATE, DELETE та DDL у базі заборонені. Після зупинки перевірити DB audit log за час пілоту; очікуваний результат — `0` write-операцій.
- Якщо audit log містить хоча б одну write-операцію, негайно відкликати DB write-права пілоту, зберегти список affected records і відновити їх із pre-pilot DB snapshot під контролем Марії Фоміної. Не виконувати ручне масове видалення без її схвалення.
- Якщо pre-pilot DB snapshot для affected records відсутній, залишити записи незмінними, заблокувати подальший доступ пілоту та передати інцидент власнику даних для погодження окремої процедури відновлення. Власник дії: Марія Фоміна.

## Deployment rollback

- Production deploy не виконується. Для pilot environment зупинити deployment/job, що запустив Boot 3 worktree, і розгорнути останній справний артефакт, зібраний із базового коміту `a1b9f3c`.
- Перевірити health endpoint і контрольне читання звіту після redeploy; production environment не чіпати. Власник дії: Сергій Громов.

## Abort conditions

Пілот зупиняється та запускається rollback, якщо виконується будь-яка з умов нижче:

1. Частка HTTP 5xx або помилок читання звітів перевищує **2% за ковзне 15-хвилинне вікно**.
2. **p95 latency читання звіту** перевищує pre-pilot baseline більш ніж на **20% протягом 10 послідовних хвилин**.
3. У DB audit log з'являється **хоча б 1** INSERT, UPDATE, DELETE або DDL від імені pilot service account.
4. Health check Boot 3 instance падає **3 послідовні рази** з інтервалом **30 секунд** або instance перезапускається **понад 2 рази за 10 хвилин**.
5. Будь-яка операція виходить за межі scope: звернення до `billing`/`auth` або записова операція; такий факт фіксується як негайна причина abort незалежно від метрик.

Порядок після спрацювання умови: зафіксувати timestamp і метрики, вимкнути `boot3_pilot_enabled`, зупинити pilot instance, виконати потрібні code/dependency/config/data/deployment rollback actions і лише потім проводити повторний аналіз.

## Owner approval

- Перед стартом потрібне фінальне схвалення Анни Котової як власниці модуля `reports`.
- Для виконання відповідного шару rollback потрібне підтвердження власника шару: Ігор Лебедєв (збірка/конфіги), Марія Фоміна (дані/схема БД), Сергій Громов (deployment/CI), Анна Котова (код `reports`).
- Data rollback із відновленням snapshot не виконується без окремого схвалення Марії Фоміної; deployment rollback виконується Сергієм Громовим після фіксації abort condition.