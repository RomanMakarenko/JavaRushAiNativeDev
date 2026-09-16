# Заплановані зміни конфігів у pilot

- У `application-pilot.yml` додається feature flag `boot3_pilot_enabled` (за замовчуванням `false`)
- Змінюється версія Spring Boot у `build.gradle.kts`: 2.7.x → 3.x
- Оновлюється `gradle.lockfile` під нові залежності
- Зміни даних НЕ плануються: pilot працює в режимі read-only
- Зміни deployment-конфігів НЕ плануються: pilot не деплоїться