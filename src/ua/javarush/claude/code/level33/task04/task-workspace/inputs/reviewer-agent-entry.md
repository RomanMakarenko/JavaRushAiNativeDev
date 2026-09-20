# Workflow — reviewer subagent

- Domain: developer workflow / automation.
- Проблема: ручний Layer 2 review був непослідовним і пропускав надто широкі diff.
- Зона відповідальності розробника: дизайн ролі агента, обмеження tools до read і запуску тестів, формат output.
- Роль AI: виконання review за заданим контрактом; рішення приймає людина.
- Verification: агент прогнаний на кількох прикладах PR, зауваження звірені вручну.
- Result: повторюваний структурований review із конкретними рядками.
- Публічне посилання: https://github.com/example/workflow-kit
- Limitations: агент не замінює human approval, лише готує зауваження.