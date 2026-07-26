package com.acme.team;

import java.util.Set;

/**
 * Перевіряє, чи не обіцяє conceptual team більше, ніж може гарантувати.
 *
 * Для conceptual team допустиме лише read-only investigation: фінальне
 * рішення, review і merge залишаються за людиною.
 *
 * У забороненому наборі: AUTO_MERGE, NO_REVIEW, AUTONOMOUS_PRODUCTION.
 * Допустиме лише READ_ONLY_INVESTIGATION.
 */
public class TeamPromiseValidator {

    private static final Set<TeamPromise> FORBIDDEN = Set.of(
            TeamPromise.AUTO_MERGE,
            TeamPromise.NO_REVIEW,
            TeamPromise.AUTONOMOUS_PRODUCTION
    );

    /**
     * Повертає true, якщо обіцянка заборонена для conceptual team.
     */
    public boolean isForbidden(TeamPromise promise) {
        return FORBIDDEN.contains(promise);
    }

    /**
     * Повертає true, якщо обіцянка допустима для conceptual team.
     */
    public boolean isAllowed(TeamPromise promise) {
        return !isForbidden(promise);
    }
}