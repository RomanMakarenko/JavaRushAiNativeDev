#!/usr/bin/env python3
"""Простий валідатор hook-конфігурації.

Перевіряє, що файл є валідним JSON і містить обов'язкові поля
event і handler. Завершується з exit code 0 у разі успіху і 1 у разі помилки.
"""
import json
import sys


def main() -> int:
    if len(sys.argv) != 2:
        print("usage: validate_hook_json.py <path-to-hook.json>", file=sys.stderr)
        return 1

    path = sys.argv[1]
    try:
        with open(path, "r", encoding="utf-8") as f:
            data = json.load(f)
    except FileNotFoundError:
        print(f"FAIL: файл не знайдено: {path}", file=sys.stderr)
        return 1
    except json.JSONDecodeError as exc:
        print(f"FAIL: невалідний JSON: {exc}", file=sys.stderr)
        return 1

    if "event" not in data:
        print("FAIL: відсутнє поле event", file=sys.stderr)
        return 1
    if "handler" not in data or "command" not in data.get("handler", {}):
        print("FAIL: відсутній handler.command", file=sys.stderr)
        return 1

    print(f"OK: {path} — event={data['event']}")
    return 0


if __name__ == "__main__":
    sys.exit(main())