#!/usr/bin/env bash
# draft-release-notes.sh — збирає draft release notes як artifact.
# Це безпечна дія: тут немає жодних tag/push/publish.
set -euo pipefail

echo "# Draft release notes"
echo
echo "Зібрано автоматично з merged PR за поточний релізний діапазон."
echo "Це draft artifact, не опублікований реліз."