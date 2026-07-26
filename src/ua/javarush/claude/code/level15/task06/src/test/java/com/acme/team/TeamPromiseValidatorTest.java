package com.acme.team;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TeamPromiseValidatorTest {

    private final TeamPromiseValidator validator = new TeamPromiseValidator();

    @Test
    void readOnlyInvestigationIsAllowed() {
        // Безпечна обіцянка про read-only investigation має залишатися допустимою.
        assertTrue(validator.isAllowed(TeamPromise.READ_ONLY_INVESTIGATION));
    }

    // TODO: додати regression tests на заборонені обіцянки
    // AUTO_MERGE, NO_REVIEW, AUTONOMOUS_PRODUCTION.
    @Test
    void autoMergeIsForbidden() {
        assertFalse(validator.isForbidden(TeamPromise.AUTO_MERGE));
    }
}