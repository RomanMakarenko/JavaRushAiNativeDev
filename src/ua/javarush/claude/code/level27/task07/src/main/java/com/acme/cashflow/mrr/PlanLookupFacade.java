package com.acme.cashflow.mrr;

import org.springframework.stereotype.Component;

/**
 * Один вхід для решти коду: зовні відомий лише find(...).
 * Усередині фасад вирішує, іти в legacy чи в v2. Умовна логіка
 * перемикання має жити тут і більше ніде.
 *
 * TODO: завершити routing — спрямовувати виклик у PlanLookupV2 лише при
 * увімкненому прапорці і singlePlan=true. Public contract методу find не змінювати.
 */
@Component
public class PlanLookupFacade {

    private final LegacyPlanLookup legacy;
    private final PlanLookupV2 modern;
    private final MrrFeatureFlags flags;

    public PlanLookupFacade(LegacyPlanLookup legacy, PlanLookupV2 modern, MrrFeatureFlags flags) {
        this.legacy = legacy;
        this.modern = modern;
        this.flags = flags;
    }

    public PlanInfo find(String code, boolean singlePlan) {
        if (flags.useV2ForSinglePlan() && singlePlan) {
            return modern.find(code);
        }
        return legacy.find(code);
    }
}