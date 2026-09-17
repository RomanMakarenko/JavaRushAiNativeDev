# EVIDENCE_LOG.md — Refund Assistant

Сліди виконання: що перевіряли, що виявили, який висновок.

## Розбіжність smoke-скрипта з актуальним demo-endpoint

**Що перевіряли:** чи відповідає verification path актуальному core flow demo.

**Що виявили:** `scripts/smoke-demo.sh` звертається до `GET /api/refund/check`, а `app/main.py` реалізує актуальний `GET /api/demo/status`. `tests/test_smoke_script.py` також перевіряє застарілий шлях `/api/refund/check`.

**Висновок:** розбіжність зафіксовано як known limitation у `SPEC.md`; до виправлення актуальний verification path проходить вручну через `/api/demo/status`, повз smoke-скрипт.

**Рішення щодо правки:** правку `scripts/smoke-demo.sh` і `tests/test_smoke_script.py` відкладено у фазу реалізації. `scripts/smoke-demo.sh`, `tests/test_smoke_script.py` та `app/main.py` на цьому проході не змінювалися.