"""Тести для calc_verdict.

Перевіряють, що verdict 'advanced workflow justified' видається ТІЛЬКИ при
зелених тестах і rework_after_merge == 0, а в усіх інших випадках — обережний verdict.
У поточній (багованій) версії скрипта ці тести падають.
"""

import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "scripts"))

from calc_verdict import calc_verdict  # noqa: E402


def test_green_and_no_rework_is_justified():
    metrics = {"tests_green": True, "rework_after_merge": 0, "conflicts": 0}
    assert calc_verdict(metrics) == "advanced workflow justified"


def test_green_but_rework_is_not_justified():
    metrics = {"tests_green": True, "rework_after_merge": 2, "conflicts": 1}
    assert calc_verdict(metrics) != "advanced workflow justified"


def test_failed_tests_is_not_justified():
    metrics = {"tests_green": False, "rework_after_merge": 0, "conflicts": 0}
    assert calc_verdict(metrics) != "advanced workflow justified"