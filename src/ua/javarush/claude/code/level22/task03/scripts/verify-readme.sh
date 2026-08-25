#!/usr/bin/env bash
# docs-as-code перевірка: README.md містить актуальні команди.
# Код виходу 0 — успіх, 1 — команда застаріла або README не знайдено.

set -u

# Команди, які мають бути присутні в README.md
REQUIRED_COMMANDS=(
  "docker compose up -d"
  "./gradlew test"
  "npm run lint"
)

# Скрипт лежить у scripts/, README.md — рівнем вище
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
README="$SCRIPT_DIR/../README.md"

if [[ ! -f "$README" ]]; then
  echo "Помилка: не знайдено $README" >&2
  exit 1
fi

failures=0

for cmd in "${REQUIRED_COMMANDS[@]}"; do
  if ! grep -Fq "$cmd" "$README"; then
    echo "Не знайдено команду в README.md: $cmd" >&2
    failures=$((failures + 1))
  fi
done

if (( failures > 0 )); then
  echo "docs:check не пройшов: $failures команда(и) відсутня(і) в README.md" >&2
  exit 1
fi

echo "docs:check пройшов: усі команди актуальні в README.md"
exit 0