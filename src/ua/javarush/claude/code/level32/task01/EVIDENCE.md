# EVIDENCE — підтвердження результату

Нижче зафіксовано факти відтворюваного запуску mini-capstone.

## Підняття сервісу

```bash
$ docker compose up -d --build
[+] Running 1/1
 ✔ Container ticket-status  Started
```

## Health-check

```bash
$ curl http://localhost:18080/health
{"status":"ok"}
```

## Тести

```bash
$ pytest -q
..                                                          [100%]
2 passed in 0.41s
```

Усі ключові сценарії з `SPEC.md` підтверджено командами вище.