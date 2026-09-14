package com.acme.cashflow.mrr;

import org.springframework.stereotype.Component;

/**
 * Окремий вхід для решти коду: зовні відомий лише find(...).
 * Усередині фасад вирішує, йти в legacy чи у v2. Умовна логіка
 * перемикання живе тут і більше ніде, щоб не розповзатися по коду.
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

    /**
     * Публічний контракт фасаду. Не змінюється під час увімкнення/вимкнення slice.
     * Новий шлях спрацьовує лише для вузького випадку single-plan і лише під прапорцем.
     */
    public PlanInfo find(String code, boolean singlePlan) {
        if (singlePlan && flags.useV2ForSinglePlan()) {
            return modern.find(code);
        }
        return legacy.find(code);
    }
}