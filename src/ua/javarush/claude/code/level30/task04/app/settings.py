"""Конфігурація сервісу класифікації тікетів.

ПРОБЛЕМА (вихідний стан): за замовчуванням вибрано зовнішній платний provider,
а API token є обов'язковим. Для demo це крихка опора — без живого зовнішнього
сервісу конфігурація не піднімається.
"""

import os


class Settings:
    """Налаштування classifier-сервісу."""

    def __init__(self) -> None:
        # За замовчуванням використовується зовнішній платний провайдер.
        self.classifier_provider = os.getenv("CLASSIFIER_PROVIDER", "topclass-api")
        # Token читається з оточення і вважається обов'язковим.
        self.classifier_api_token = os.getenv("CLASSIFIER_API_TOKEN")

        if self.classifier_provider == "topclass-api" and not self.classifier_api_token:
            # Зовнішній token є обов'язковим для запуску — demo падає без нього.
            raise RuntimeError("CLASSIFIER_API_TOKEN is required")


def get_settings() -> Settings:
    return Settings()