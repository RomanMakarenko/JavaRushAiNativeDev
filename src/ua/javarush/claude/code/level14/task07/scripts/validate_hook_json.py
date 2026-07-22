#!/usr/bin/env python3
"""Мінімальний валідатор конфігів hooks.

Перевіряє, що переданий файл — дійсний JSON і містить обов'язкові поля:
name, event, matcher, handler. Завершується ненульовим кодом у разі помилки.
"""
import json
import sys

REQUIRED = ("name", "event", "matcher", "handler")


def main() -> int:
    if len(sys.argv) != 2:
        print("використання: validate_hook_json.py <path-to-hook.json>", file=sys.stderr)
        return 2
    path = sys.argv[1]
    try:
        with open(path, encoding="utf-8") as f:
            data = json.load(f)
    except (OSError, json.JSONDecodeError) as exc:
        print(f"недійсний JSON у {path}: {exc}", file=sys.stderr)
        return 1
    missing = [key for key in REQUIRED if key not in data]
    if missing:
        print(f"відсутні обов'язкові поля: {', '.join(missing)}", file=sys.stderr)
        return 1
    print(f"OK: {path} дійсний")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())