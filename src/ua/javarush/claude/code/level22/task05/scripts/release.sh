#!/usr/bin/env bash
# release.sh — release automation для acme-billing-service.
#
# УВАГА (початковий стан): script одразу створює tag і публікує release
# без будь-якого human approval. Це порушує межу artifact vs action.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
VERSION="$(cat "$ROOT/VERSION.txt")"

echo "Releasing version v${VERSION}"

# Збірка draft notes
{
  echo "# Draft release notes"
  echo
  echo "Release v${VERSION}"
} > "$ROOT/release/DRAFT_NOTES.md"

# Небезпечна частина: tagging і publish виконуються автоматично
git tag "v${VERSION}"
git push origin "v${VERSION}"
gh release create "v${VERSION}" --notes-file "$ROOT/release/DRAFT_NOTES.md"

echo "Done: v${VERSION} tagged and published"