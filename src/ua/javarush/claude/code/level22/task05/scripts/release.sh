#!/usr/bin/env bash
# release.sh — release automation для acme-billing-service.
#
# За замовчуванням працює в dry-run режимі (артефакт, а не дія):
#   * пропонує версію з VERSION.txt;
#   * будує чернетку нотаток у release/DRAFT_NOTES.md;
#   * НЕ створює тег, НЕ робить push, НЕ публікує release.
#
# Реальний tagging/publish — окрема дія, тільки з явним прапором
# --publish і підтвердженням людини (human approval).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
VERSION="$(cat "$ROOT/VERSION.txt")"
MODE="${1:-dry-run}"

build_draft_notes() {
  cat > "$ROOT/release/DRAFT_NOTES.md" <<EOF
# Draft release notes

Release v${VERSION}

Підготовлено в dry-run режимі, не є опублікованим релізом.
Tagging і publish вимагають окремого human approval.
EOF
}

run_dry_run() {
  echo "== Dry-run: пропонована версія v${VERSION}"
  echo "== Dry-run: будую release/DRAFT_NOTES.md"
  build_draft_notes
  echo
  echo "ВАЖЛИВО: tagging і publish потребують окремого human approval."
  echo "Цей прогон НЕ створював тег і НЕ публікував release — жодних змін у git/gh."
  echo "Щоб виконати реальний release з підтвердженням: $0 --publish"
}

publish_release() {
  # Дії поза цим скриптом. Виконуються лише після --publish
  # і явного підтвердження людини нижче.
  local GIT GH
  GIT=git
  GH=gh
  "$GIT" tag "v${VERSION}"
  "$GIT" push origin "v${VERSION}"
  "$GH" release create "v${VERSION}" --notes-file "$ROOT/release/DRAFT_NOTES.md"
  echo "Done: v${VERSION} tagged and published"
}

case "$MODE" in
  --dry-run|-n|"")
    run_dry_run
    ;;
  --publish)
    build_draft_notes
    echo "Реальний publish версії v${VERSION}."
    echo "Це створить git tag, зробить git push origin і опублікує GitHub release."
    read -r -p "Підтверджуєш? Введи YES: " CONFIRM || CONFIRM=""
    if [[ "$CONFIRM" != "YES" ]]; then
      echo "Скасовано. Нічого не опубліковано."
      exit 0
    fi
    publish_release
    ;;
  *)
    echo "Невідомий режим: $MODE" >&2
    echo "Використання: $0 [--dry-run|--publish]" >&2
    exit 1
    ;;
esac