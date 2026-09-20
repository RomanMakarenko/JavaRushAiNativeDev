# Interview prep page

Невеликий застосунок Next.js, який показує картки для підготовки до
інтерв’ю: відповідь на запитання «did you build this, or did AI?» і список
follow-up запитань.

Контекст: сторінка спирається на capstone-проєкт **AI Commerce Growth OS**
(модуль refund inbox, задача RF-217).

## Проблема

Зараз відповідь показано не повністю: немає блоку `What I verified manually`,
follow-up запитань менше п’яти, а в даних залишився overclaim про роль Claude.
Через це сторінка виглядає як маркетингова, а не інженерна.

## Запуск тестів

```bash
npm test
```

## Файли

- `data/interview.ts` — дані відповіді та список follow-up запитань.
- `app/page.tsx` — сторінка, яка рендерить картки.
- `tests/interview-page.test.tsx` — тести сторінки.