# refund-service — release tooling

Сервіс обробки повернень. У каталозі `scripts/` лежить допоміжний скрипт для релізу.

## scripts/release-helper.sh

Запускається для різних середовищ через змінну `TARGET_ENV`:

```bash
TARGET_ENV=staging ./scripts/release-helper.sh
TARGET_ENV=production ./scripts/release-helper.sh
```

Production deploy належить до етапу ручного погодження Layer 3 і не повинен
виконуватися автоматично з допоміжного скрипта.

Поточний реліз: `v2.31.0` (виправлення черги повернень).