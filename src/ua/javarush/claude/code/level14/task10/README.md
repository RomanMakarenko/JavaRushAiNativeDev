# Workflow Kit — хуки

Набір хуків автоматизації для проєкту AI Commerce Growth OS.

## format-on-edit

Хук `format-on-edit` форматує змінений frontend-файл через `prettier`
після редагування. Конфіг лежить у `.claude/hooks/format-on-edit.json`,
handler — у `scripts/format-on-edit.sh`.

### Відома проблема

Matcher був випадково розширений до `frontend/src/**/*`, через що хук почав
спрацьовувати на згенерованих файлах (`*.generated.ts`, `*.generated.tsx`) і шуміти.
Робочий baseline — вузький matcher лише для frontend TypeScript-файлів.

### Перевірка конфіга

```bash
python3 scripts/validate_hook_json.py .claude/hooks/format-on-edit.json
```

Команда завершується з кодом 0, якщо JSON валідний і matcher достатньо вузький.