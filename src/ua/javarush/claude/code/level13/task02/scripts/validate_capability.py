#!/usr/bin/env python3
"""Валідатор мінімального MCP capability contract.

Перевіряє, що JSON-файл описує зовнішню capability рівно за базовою моделлю
теми: зовнішній сервер, список read-only tools і режим доступу. Зайві мережеві й
auth-деталі на цьому рівні заборонені.

Використання:
    python scripts/validate_capability.py mcp/issue-tracker-draft.json

Друкує PASS, якщо всі перевірки пройдено, інакше FAIL і список причин.
"""
import json
import sys

REQUIRED_KEYS = ["kind", "server", "tools", "access"]
FORBIDDEN_KEYS = ["transport", "endpoint", "auth", "authScopes"]
REQUIRED_TOOLS = ["list_issues", "get_issue", "search_issues"]


def validate(path):
    errors = []
    try:
        with open(path, "r", encoding="utf-8") as f:
            data = json.load(f)
    except (OSError, json.JSONDecodeError) as exc:
        return [f"не вдалося прочитати JSON: {exc}"]

    if not isinstance(data, dict):
        return ["корінь JSON має бути об'єктом"]

    # обов'язкові ключі
    for key in REQUIRED_KEYS:
        if key not in data:
            errors.append(f"відсутній обов'язковий ключ: {key}")

    # заборонені ключі (transport/auth/scopes на цьому рівні не потрібні)
    for key in FORBIDDEN_KEYS:
        if key in data:
            errors.append(f"заборонене поле має бути видалено: {key}")

    # точні значення
    if data.get("kind") != "external-capability":
        errors.append("kind має бути 'external-capability'")
    if data.get("server") != "commerce-issue-tracker":
        errors.append("server має бути 'commerce-issue-tracker'")
    if data.get("access") != "read-only":
        errors.append("access має бути 'read-only'")

    # список tools
    tools = data.get("tools")
    if not isinstance(tools, list):
        errors.append("tools має бути масивом")
    else:
        for tool in REQUIRED_TOOLS:
            if tool not in tools:
                errors.append(f"у tools відсутній: {tool}")

    return errors


def main():
    if len(sys.argv) != 2:
        print("FAIL")
        print("usage: python scripts/validate_capability.py <path-to-json>")
        return 2

    errors = validate(sys.argv[1])
    if errors:
        print("FAIL")
        for err in errors:
            print(f"- {err}")
        return 1

    print("PASS")
    return 0


if __name__ == "__main__":
    sys.exit(main())