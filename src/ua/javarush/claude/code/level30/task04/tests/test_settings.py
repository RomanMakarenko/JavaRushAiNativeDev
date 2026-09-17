"""Тести demo-конфігурації classifier-сервісу.

КОНТЕКСТ (affected area): ці тести показують, яку поведінку demo-slice ми
описуємо в MVP_SPEC.md — дефолтний local provider без обов'язкового token.
На етапі проєктування код і тести не редагуються.
"""

import os

from app.services.classifier_client import ClassifierClient
from app.settings import get_settings


def test_default_provider_is_local(monkeypatch):
    # У demo-режимі без зовнішніх змінних оточення provider має бути локальним.
    monkeypatch.delenv("CLASSIFIER_PROVIDER", raising=False)
    monkeypatch.delenv("CLASSIFIER_API_TOKEN", raising=False)

    settings = get_settings()

    assert settings.classifier_provider == "local-sample"


def test_demo_starts_without_api_token(monkeypatch):
    # Запуск demo-конфігурації не повинен вимагати зовнішній API token.
    monkeypatch.delenv("CLASSIFIER_PROVIDER", raising=False)
    monkeypatch.delenv("CLASSIFIER_API_TOKEN", raising=False)

    settings = get_settings()

    assert settings.classifier_api_token is None


def test_local_classify_works_offline(monkeypatch):
    # Локальний stub має повертати тему без звернення до мережі.
    monkeypatch.delenv("CLASSIFIER_PROVIDER", raising=False)
    monkeypatch.delenv("CLASSIFIER_API_TOKEN", raising=False)

    client = ClassifierClient(get_settings())

    assert client.classify("хочу оформити повернення") == "refund"