#!/usr/bin/env bash
# release-helper для refund-service v2.31.0
# Запуск: TARGET_ENV=<env> ./scripts/release-helper.sh
set -euo pipefail

TARGET_ENV="${TARGET_ENV:-staging}"

echo "release-helper: target env = ${TARGET_ENV}"

# Збірка артефакту однакова для всіх середовищ
build_artifact() {
  echo "збірка release-артефакту для ${TARGET_ENV}..."
}

# Реальний deploy
run_deploy() {
  echo "виконую deploy у ${TARGET_ENV}..."
  # тут виконується реальна команда деплою
}

build_artifact

# ПРОБЛЕМА: deploy виконується для будь-якого середовища,
# включно з production — без межі ручного погодження
run_deploy

echo "release-helper: готово"