# Маніфест базових резервних копій

Резервні копії зняті до старту пілоту і зберігаються в `backups/`:

| Файл резервної копії             | Джерело                       |
|----------------------------------|-------------------------------|
| `build.gradle.kts.pre`           | `build.gradle.kts`            |
| `gradle.lockfile.pre`            | `gradle.lockfile`             |
| `application-pilot.yml.pre`      | `config/application-pilot.yml` |

Базовий коміт (короткий хеш): `a1b9f3c`