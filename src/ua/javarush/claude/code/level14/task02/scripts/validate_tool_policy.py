#!/usr/bin/env python3
"""Валідатор preflight policy зовнішнього інструмента.

Перевіряє, що policy-файл відповідає read-only baseline команди:
заборонені write actions видалені, режим read-only, є kill switch,
verification rule і обмежений scope.

Використання:
    python3 scripts/validate_tool_policy.py .claude/tool-policies/issue-tracker.json
"""
import json
import sys

# Дії, які не входять до read-only baseline.
WRITE_ACTIONS = {
    "commentIssue", "closeIssue", "reassign", "changeLabels",
    "ackAlert", "silenceAlert", "publish",
}


def fail(message: str) -> None:
    print(f"FAIL: {message}")
    sys.exit(1)


def main() -> None:
    if len(sys.argv) != 2:
        fail("очікується рівно один аргумент — шлях до policy-файлу")

    path = sys.argv[1]
    try:
        with open(path, encoding="utf-8") as handle:
            policy = json.load(handle)
    except FileNotFoundError:
        fail(f"файл не знайдено: {path}")
    except json.JSONDecodeError as error:
        fail(f"невалідний JSON: {error}")

    if policy.get("mode") != "read-only":
        fail("режим повинен бути read-only")

    actions = set(policy.get("allowedActions", []))
    forbidden = actions & WRITE_ACTIONS
    if forbidden:
        fail(f"в allowedActions залишилися write actions: {sorted(forbidden)}")

    if policy.get("scope") in (None, "", "all-projects"):
        fail("scope повинен бути обмежений конкретним проєктом")

    if not policy.get("killSwitchPath"):
        fail("не вказано killSwitchPath")

    if not policy.get("verificationRule"):
        fail("не вказано verificationRule")

    print("OK: write actions відсутні, policy відповідає read-only baseline")
    sys.exit(0)


if __name__ == "__main__":
    main()