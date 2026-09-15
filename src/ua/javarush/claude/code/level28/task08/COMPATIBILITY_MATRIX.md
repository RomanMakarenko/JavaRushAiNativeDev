# COMPATIBILITY_MATRIX.md — CashFlow Dashboard

<!--
Заповніть ОДИН рядок-блок для залежності з динамічною маскою.
Лише для читання: build.gradle не змінюємо. Точну версію беремо з evidence/dependencies.txt.
Заміщення маски на точну версію опишіть у «Потрібна дія» як ПЛАН.
-->

## Компонент
<!-- наприклад: org.hibernate:hibernate-core -->

## Поточна версія
<!-- точне число з виводу ./gradlew dependencies; зазначте, що в build.gradle стоїть маска 5.+ -->

## Цільова версія
<!-- фіксація current state (та сама лінія); upgrade-таргет тут не обираємо -->

## Статус сумісності
<!-- один зі словника: safe update / requires intermediate version / blocked / needs replacement / needs code changes / needs config changes / needs manual validation -->

## Ризик
<!-- чому динамічна маска — погана baseline для матриці -->

## Потрібна дія
<!-- ПЛАН: замінити маску на точну версію в тому самому рядку. Не виконувати правку тут. -->

## Обмеження послідовності
<!-- що має статися раніше/пізніше -->

## Докази
<!-- build.gradle (рядок із маскою) + evidence/dependencies.txt (вивід dependencies) -->