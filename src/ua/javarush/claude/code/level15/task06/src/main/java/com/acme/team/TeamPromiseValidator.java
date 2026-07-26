package com.acme.team;

import java.util.Set;

/**
 * Перевіряє, чи не обіцяє conceptual team більше, ніж може гарантувати.
 *
 * Для conceptual team допустиме лише read-only investigation: фінальне
 * рішення, review і merge залишаються за людиною.
 *
 * BUG: поточний набір forbidden занадто вузький — небезпечні обіцянки AUTO_MERGE,
 * NO_REVIEW і AUTONOMOUS_PRODUCTION проходять як допустимі.
 */
public class TeamPromiseValidator {

    // Зараз тут немає жодної справді небезпечної обіцянки,
    // тому валідатор пропускає все підряд.
    private static final Set<TeamPromise> FORBIDDEN = Set.of();

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