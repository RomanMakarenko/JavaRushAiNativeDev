"""Локальна перевірка: перевірка налаштування root_path."""

from app.main import app


def test_root_path_matches_proxy_prefix():
    # Зовнішній префікс проксі — /api, root_path має збігатися
    assert app.root_path == "/api"