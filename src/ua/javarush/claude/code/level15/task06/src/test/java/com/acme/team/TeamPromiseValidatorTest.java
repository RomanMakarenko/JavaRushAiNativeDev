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

    @Test
    void autoMergeIsForbidden() {
        assertFalse(validator.isAllowed(TeamPromise.AUTO_MERGE));
        assertTrue(validator.isForbidden(TeamPromise.AUTO_MERGE));
    }

    @Test
    void noReviewIsForbidden() {
        assertFalse(validator.isAllowed(TeamPromise.NO_REVIEW));
        assertTrue(validator.isForbidden(TeamPromise.NO_REVIEW));
    }

    @Test
    void autonomousProductionIsForbidden() {
        assertFalse(validator.isAllowed(TeamPromise.AUTONOMOUS_PRODUCTION));
        assertTrue(validator.isForbidden(TeamPromise.AUTONOMOUS_PRODUCTION));
    }
}