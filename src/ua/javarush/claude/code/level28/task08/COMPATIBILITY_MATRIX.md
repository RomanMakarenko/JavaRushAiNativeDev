# COMPATIBILITY_MATRIX.md — CashFlow Dashboard

<!--
Заповніть ОДИН рядок-блок для залежності з динамічною маскою.
Лише для читання: build.gradle не змінюємо. Точну версію беремо з evidence/dependencies.txt.
Заміщення маски на точну версію опишіть у «Потрібна дія» як ПЛАН.
-->

## Компонент
`org.hibernate:hibernate-core`

## Поточна версія
`5.6.15.Final` — фактичний current state, отриманий із виводу `./gradlew dependencies`; у `build.gradle` задано динамічну маску `5.+`.

## Цільова версія
`5.6.15.Final` як зафіксований current state для discovery; upgrade-таргет не обирається.

## Статус сумісності
`needs manual validation`

## Ризик
Маска `5.+` може резолвитися в іншу версію під час наступної збірки, тому поточний результат не є надійним і відтворюваним baseline для матриці сумісності.

## Потрібна дія
**ПЛАН:** замінити маску `5.+` на точну версію `5.6.15.Final` у тому самому рядку `build.gradle`. У межах цього discovery правку не виконувати.

## Обмеження послідовності
Спочатку зафіксувати точну версію в залежності, потім окремо виконати перевірку сумісності; цільову версію для upgrade визначати лише після цього discovery.

## Докази
- `build.gradle:24` — `implementation 'org.hibernate:hibernate-core:5.+'`.
- `evidence/dependencies.txt:6` — `org.hibernate:hibernate-core:5.+ -> 5.6.15.Final` у `compileClasspath`.
- `evidence/dependencies.txt:11-13` — пояснення, що `5.+` резолвиться в `5.6.15.Final` на момент збирання evidence.