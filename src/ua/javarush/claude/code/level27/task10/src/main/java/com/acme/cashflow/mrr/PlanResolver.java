package com.acme.cashflow.mrr;

import java.time.LocalDate;
import java.util.List;

/**
 * Визначає активний план підписки на задану дату.
 * Legacy-код: містить дублювальну логіку lookup, кандидат на seam.
 */
public class PlanResolver {

    /** Обирає активний план на вказану дату (legacy-шлях). */
    public Plan resolvePlan(List<Plan> plans, LocalDate onDate) {
        Plan active = null;
        for (Plan plan : plans) {
            boolean started = !onDate.isBefore(plan.startDate());
            boolean notEnded = plan.endDate() == null || !onDate.isAfter(plan.endDate());
            if (started && notEnded) {
                // legacy quirk: при overlapping-підписках беремо останній підхожий
                active = plan;
            }
        }
        return active;
    }
}