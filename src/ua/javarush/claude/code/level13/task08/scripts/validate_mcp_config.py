#!/usr/bin/env python3
"""Валідатор project-scoped MCP-конфіга issue tracker.

Перевіряє, що конфіг приведено до акуратного read-only baseline:
- присутні всі обов'язкові поля;
- значення name/transport/auth/scope відповідають очікуваним;
- endpoint береться зі змінної середовища ISSUE_TRACKER_URL;
- authScopes звужені строго до read:issues;
- усі tools позначені read-only;
- у файлі немає захардкожених секретів.

Використання:
    python scripts/validate_mcp_config.py mcp/issue-tracker.json
"""
import json
import re
import sys

REQUIRED_FIELDS = ["name", "transport", "endpoint", "auth", "authScopes", "tools", "scope", "notes"]
# маркери secret-like значень прямо всередині файла
SECRET_MARKERS = ["sk_live", "sk_test", "Bearer ", "api_key", "\"token\""]


def fail(message):
    print("FAIL")
    print("  причина:", message)
    sys.exit(1)


def main():
    if len(sys.argv) != 2:
        fail("очікується рівно один аргумент — шлях до JSON-конфіга")

    path = sys.argv[1]
    try:
        with open(path, encoding="utf-8") as handle:
            raw = handle.read()
    except OSError as error:
        fail(f"не вдалося відкрити файл: {error}")

    # секрети не мають лежати прямо в конфігу
    for marker in SECRET_MARKERS:
        if marker in raw:
            fail(f"у файлі знайдено secret-like маркер: {marker}")

    try:
        config = json.loads(raw)
    except json.JSONDecodeError as error:
        fail(f"некоректний JSON: {error}")

    for field in REQUIRED_FIELDS:
        if field not in config:
            fail(f"відсутнє обов'язкове поле: {field}")

    if config["name"] != "issue-tracker":
        fail("поле name має бути 'issue-tracker'")

    if config["transport"] != "http":
        fail("поле transport має бути 'http'")

    if not re.search(r"\$\{?ISSUE_TRACKER_URL\}?", str(config["endpoint"])):
        fail("поле endpoint має посилатися на змінну середовища ISSUE_TRACKER_URL")

    if config["auth"] != "oauth":
        fail("поле auth має бути 'oauth'")

    if config["authScopes"] != ["read:issues"]:
        fail("authScopes мають містити лише 'read:issues'")

    if config["scope"] != "project":
        fail("поле scope має бути 'project'")

    tools = config["tools"]
    if not isinstance(tools, list) or not tools:
        fail("поле tools має бути непорожнім списком")
    for tool in tools:
        if tool.get("permissions") != "read-only":
            fail(f"інструмент '{tool.get('name')}' має бути read-only")

    print("PASS")
    sys.exit(0)


if __name__ == "__main__":
    main()