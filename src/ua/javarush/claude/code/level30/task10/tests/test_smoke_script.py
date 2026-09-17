# Тест перевіряє, що smoke-скрипт звертається до demo-endpoint.
# УВАГА: тест зафіксував застарілий шлях /api/refund/check.


def read_script():
    with open("scripts/smoke-demo.sh", encoding="utf-8") as f:
        return f.read()


def test_script_uses_endpoint():
    text = read_script()
    assert "/api/refund/check" in text