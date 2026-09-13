#!/usr/bin/env bash
# discovery-scan.sh — огляд legacy-проєкту для оцінювання change risk.
# Збирає top-level modules, config files, test files і SQL migrations.

set -uo pipefail

# Шляхи, які виключаємо з огляду (артефакти збірки та залежності).
PRUNE='-path ./build -o -path ./.gradle -o -path ./node_modules'

print_section() {
    # $1 — заголовок розділу, далі построково подаються шляхи на stdin
    echo "$1:"
    while IFS= read -r line; do
        [ -n "$line" ] && echo "  $line"
    done
    echo
}

echo "=== discovery-scan ==="
echo

# Top-level modules: каталоги верхнього рівня без build/.gradle/node_modules
find . -mindepth 1 -maxdepth 1 -type d \
    ! -name build ! -name .gradle ! -name node_modules ! -name .git \
    | sort | print_section "Top-level modules"

# Config files: типові конфіги застосунку та збірки
find . \( $PRUNE \) -prune -o \
    -type f \( -name '*.yml' -o -name '*.yaml' -o -name '*.properties' \
               -o -name '*.gradle' -o -name 'pom.xml' \) -print \
    | sort | print_section "Config files"

# Test files: усе з src/test, якщо каталог існує
if [ -d src/test ]; then
    find src/test -type f -name '*.java' | sort
else
    echo
fi | print_section "Test files"

# SQL migrations: міграції з src/main/resources та db/migration
find . \( $PRUNE \) -prune -o \
    -type f -name '*.sql' \
    \( -path '*/src/main/resources/*' -o -path '*/db/migration/*' \) -print \
    | sort | print_section "SQL migrations"

exit 0