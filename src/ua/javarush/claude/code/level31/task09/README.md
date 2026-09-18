# Personal CFO

Інструмент для solopreneur: імпортує банківську виписку в CSV, розкладає
транзакції по категоріях і будує monthly report.

## Команди проєкту

```bash
make build     # збірка середовища
make test      # pytest -q
make demo      # прогін core flow на data/sample.csv
```

## Core flow

CSV → автоматична категоризація → monthly report.