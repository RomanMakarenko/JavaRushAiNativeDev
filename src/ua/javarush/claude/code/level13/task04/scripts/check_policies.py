#!/usr/bin/env python3
"""Перевірка policy-конфігурацій двох workflow за правилом manual first.

Використання:
    python scripts/check_policies.py skills

Виводить PASS, якщо обидві policy налаштовані коректно, інакше FAIL із причинами.
"""
import json
import sys
from pathlib import Path


def load_policy(path: Path):
    """Прочитати policy.json. Повернути (data, помилка)."""
    if not path.is_file():
        return None, f"немає файла {path}"
    try:
        return json.loads(path.read_text(encoding="utf-8")), None
    except json.JSONDecodeError as exc:
        return None, f"невалідний JSON у {path}: {exc}"


def check(skills_dir: Path):
    """Повернути список проблем (порожній список = все гаразд)."""
    problems = []

    issue_path = skills_dir / "issue-analysis" / "policy.json"
    debug_path = skills_dir / "local-debug" / "policy.json"

    issue, err = load_policy(issue_path)
    if err:
        problems.append(err)
    else:
        # issue-analysis: повторюваний intake -> read-only MCP
        if issue.get("inputMode") != "mcp-read-only":
            problems.append("issue-analysis: inputMode має бути mcp-read-only")
        if issue.get("allowExternalWrite") is not False:
            problems.append("issue-analysis: allowExternalWrite має бути false")
        if "intake" not in (issue.get("reason", "").lower()):
            problems.append("issue-analysis: у reason немає згадки про повторюваний intake")

    debug, err = load_policy(debug_path)
    if err:
        problems.append(err)
    else:
        # local-debug: одноразовий локальний контекст -> manual
        if debug.get("inputMode") != "manual":
            problems.append("local-debug: inputMode має бути manual")
        if debug.get("allowExternalWrite") is not False:
            problems.append("local-debug: allowExternalWrite має бути false")
        reason = debug.get("reason", "").lower()
        if "локальн" not in reason and "одноразов" not in reason:
            problems.append("local-debug: у reason немає згадки про одноразовий локальний контекст")

    return problems


def main():
    if len(sys.argv) != 2:
        print("FAIL: вкажіть каталог skills, наприклад: python scripts/check_policies.py skills")
        return 1
    skills_dir = Path(sys.argv[1])
    problems = check(skills_dir)
    if problems:
        print("FAIL")
        for p in problems:
            print(f"  - {p}")
        return 1
    print("PASS")
    return 0


if __name__ == "__main__":
    sys.exit(main())