"""Обчислює verdict про те, чи виправданий advanced AI-workflow на workstream.

УВАГА (баг): поточна версія видає "advanced workflow justified", якщо
тести зелені, і повністю ігнорує rework_after_merge та conflicts.
Через це workstream з відкатами після merge все одно позначається як успішний.
"""


def calc_verdict(metrics):
    """Повертає рядок verdict за словником metrics.

    Очікувані ключі metrics:
      - tests_green: bool
      - rework_after_merge: int
      - conflicts: int

    Публічний інтерфейс: функція calc_verdict(metrics) -> str.
    """
    if metrics.get("tests_green"):
        return "advanced workflow justified"
    return "advanced workflow not justified"


if __name__ == "__main__":
    sample = {"tests_green": True, "rework_after_merge": 2, "conflicts": 1}
    print(calc_verdict(sample))