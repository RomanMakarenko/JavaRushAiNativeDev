# Issue WEB-208: newsletter preferences не зберігають вибір частоти

## Що відбувається
У формі підписки на розсилку ShopFlow користувач обирає частоту листів
(`daily` / `weekly`), але під час submit на сервер завжди надходить `weekly`.

## Очікувана поведінка
На сервер має надходити саме та частота (`frequency`), яку обрав
користувач.

## Задіяний компонент
`src/components/NewsletterForm.tsx` (frontend, React + TypeScript).

## Контекст для review
Bugfix уже внесено в робоче дерево. Перед прийняттям змін потрібен review
з фокусом на scope і risks, а не правка коду.