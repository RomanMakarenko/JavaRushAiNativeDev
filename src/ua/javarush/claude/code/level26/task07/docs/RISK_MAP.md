# RISK_MAP

## Risk item: `mrr-engine / pause-resume`

- **Area:** Розрахунок MRR для активних підписок і поведінка під час pause/resume; `paused == true` зберігає MRR на рівні `lastBilledAmount`, а `resume()` використовує серверний час.
- **Business criticality:** Висока — зона визначає значення MRR, тобто впливає на фінансові метрики підписок і пов'язані billing-рішення.
- **Change risk:** Високий — pause-resume не має автоматичних тестів або звіту покриття, а останні зміни були hotfix навколо pause; зміна може непомітно змінити суму або часову поведінку MRR.
- **Risk categories:** billing/revenue correctness, regression, time-dependent behavior, observability gap.
- **Evidence:** `docs/ARCHITECTURE_CURRENT.md:4-10` фіксує freeze на `lastBilledAmount`, server clock у `resume()` та відсутність pause-resume тестів; `submissions/RISK_INPUTS.txt:7-11` — `find src/test -path '*mrr*' -name '*Test.java'` повернув порожньо; `submissions/RISK_INPUTS.txt:18-23` — `git log --oneline -- src/main/java/com/cashflow/mrr` містить `a1b2c3d hotfix: не обнуляти MRR під час pause` і `d4e5f6a feat: додати resume() із серверним часом`; `submissions/RISK_INPUTS.txt:25-27` — каталог JaCoCo для `mrr-engine` відсутній; `submissions/RISK_INPUTS.txt:13-16` — поведінка пов'язана з `mrr.engine.pause.freeze-amount` і `mrr.engine.currency` у `src/main/resources/application.properties`.
- **Missing checks:** Немає автоматичних перевірок pause, resume, freeze amount, граничних дат/часу та регресії після hotfix; немає окремого coverage report для зони.
- **Recommended action:** Не змінювати pause-resume без попереднього зафіксованого characterization-покриття поточної поведінки та рев'ю впливу на billing; до появи цих перевірок класифікувати зону як high-risk і зберігати чинну freeze semantics.