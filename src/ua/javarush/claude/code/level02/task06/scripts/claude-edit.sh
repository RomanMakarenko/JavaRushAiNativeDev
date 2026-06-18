#!/usr/bin/env bash
# claude-edit.sh — запуск Claude для звичайних локальних правок із підтвердженнями.
# Posture: default (approvals) — правки та команди потребують явного підтвердження.
set -euo pipefail

claude --permission-mode default "$@"