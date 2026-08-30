#!/usr/bin/env python3
"""Мінімальний валідатор стартового артефакта задачі Workflow Kit.

Перевіряє, що в YAML-файлі задачі присутні risk label і блок capability
envelope з обов'язковими полями та обов'язковими forbidden actions.

Запуск:
    python tools/validate-task.py workflow/new-task.yaml
"""
import sys

ALLOWED_RISK_LEVELS = {"low-risk", "review-required", "high-risk"}
REQUIRED_FORBIDDEN = {"payments/**", "migrations/**", ".env*"}


def load_yaml(path):
    try:
        import yaml  # type: ignore
        with open(path, "r", encoding="utf-8") as f:
            return yaml.safe_load(f)
    except ImportError:
        # Легкий fallback без зовнішньої залежності: грубий розбір потрібних полів.
        return _minimal_parse(path)


def _minimal_parse(path):
    """Грубий розбір без pyyaml: дістаємо riskLevel і поля capability envelope.

    Поля-списки (`allowedTools`, `requiredChecks`, `forbiddenActions`) збираються
    як список елементів; скалярні значення (`rollbackExpectation`) — як рядок.
    """
    with open(path, "r", encoding="utf-8") as f:
        lines = f.read().splitlines()

    data = {"riskLevel": None, "capabilityEnvelope": {}}
    env = data["capabilityEnvelope"]
    list_keys = ("allowedTools", "requiredChecks", "forbiddenActions")

    current_list = None  # ім'я поля, для якого зараз збираємо елементи списку
    for raw in lines:
        stripped = raw.strip()
        if not stripped or stripped.startswith("#"):
            continue

        # Елемент поточного списку ("  - item").
        if current_list is not None and stripped.startswith("- "):
            item = stripped[2:].strip().strip('"').strip("'")
            env.setdefault(current_list, []).append(item)
            continue

        # Новий ключ — закриваємо збір попереднього списку.
        current_list = None

        if stripped.startswith("riskLevel:"):
            data["riskLevel"] = stripped.split(":", 1)[1].strip()
            continue

        for key in list_keys:
            if stripped.startswith(key + ":"):
                inline = stripped.split(":", 1)[1].strip()
                if inline:
                    env[key] = [inline.strip('"').strip("'")]
                else:
                    env[key] = []
                    current_list = key  # елементи підуть наступними рядками
                break
        else:
            if stripped.startswith("rollbackExpectation:"):
                env["rollbackExpectation"] = stripped.split(":", 1)[1].strip() or "<block>"

    return data


def fail(field, message):
    print("FAIL")
    print("Field: %s" % field)
    print("Reason: %s" % message)
    sys.exit(1)


def main():
    if len(sys.argv) != 2:
        print("usage: python tools/validate-task.py <task.yaml>")
        sys.exit(2)
    data = load_yaml(sys.argv[1]) or {}

    risk = data.get("riskLevel")
    if risk is None:
        fail("riskLevel", "поле riskLevel відсутнє")
    if risk not in ALLOWED_RISK_LEVELS:
        fail("riskLevel", "недопустиме значення risk level: %s" % risk)

    env = data.get("capabilityEnvelope")
    if not env:
        fail("capabilityEnvelope", "блок capabilityEnvelope відсутній")

    for key in ("allowedTools", "requiredChecks", "forbiddenActions", "rollbackExpectation"):
        if not env.get(key):
            fail(key, "поле %s відсутнє або порожнє" % key)

    forbidden = env.get("forbiddenActions")
    forbidden_set = set(forbidden) if isinstance(forbidden, list) else set()
    missing = REQUIRED_FORBIDDEN - forbidden_set
    if missing:
        fail("forbiddenActions", "не перелічено обов'язкові шляхи: %s" % ", ".join(sorted(missing)))

    checks = env.get("requiredChecks")
    checks_text = " ".join(checks) if isinstance(checks, list) else str(checks)
    if "test" not in checks_text.lower():
        fail("requiredChecks", "у requiredChecks не вказано тести")

    print("PASS")
    print("Risk level: %s" % risk)
    print("Capability envelope: OK")
    sys.exit(0)


if __name__ == "__main__":
    main()