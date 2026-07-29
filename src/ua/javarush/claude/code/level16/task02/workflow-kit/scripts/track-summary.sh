#!/usr/bin/env bash
# Друкує зведення про паралельний трек із його manifest.
# Використання: bash scripts/track-summary.sh <path-to-track.yml>
#
# БАГ: скрипт шукає очікуваний шлях worktree замість того, щоб читати
# реальне значення з manifest, тому завжди друкує worktree=unknown.
set -euo pipefail

MANIFEST="${1:-}"
if [[ -z "$MANIFEST" || ! -f "$MANIFEST" ]]; then
  echo "manifest not found" >&2
  exit 1
fi

# Тут навмисна помилка: фіксоване unknown замість читання поля worktree.
WORKTREE="$(grep '^worktree:' "$MANIFEST" | awk '{print $2}')"
BRANCH="$(grep '^branch:' "$MANIFEST" | awk '{print $2}')"
STOP="$(grep '^stop_condition:' "$MANIFEST" | awk '{print $2}')"

echo "track=$(grep '^track:' "$MANIFEST" | awk '{print $2}')"
echo "worktree=$WORKTREE"
echo "branch=$BRANCH"
echo "stop condition=$STOP"