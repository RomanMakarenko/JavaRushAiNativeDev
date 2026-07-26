package com.acme.workflow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiAgentFitnessServiceTest {

    private final MultiAgentFitnessService service = new MultiAgentFitnessService();

    @Test
    void largeTaskWithSeparateStreamsAndTests_isJustified() {
        FitnessSignals signals = new FitnessSignals(true, 3, false, true);
        assertTrue(service.isMultiAgentJustified(signals));
    }

    @Test
    void sharedStateHigh_isNotJustified() {
        FitnessSignals signals = new FitnessSignals(true, 3, true, true);
        assertFalse(service.isMultiAgentJustified(signals));
    }

    @Test
    void noSafetyNet_isNotJustified() {
        FitnessSignals signals = new FitnessSignals(true, 3, false, false);
        assertFalse(service.isMultiAgentJustified(signals));
    }

    @Test
    void largeTaskWithOneWorkstream_isNotJustified() {
        FitnessSignals signals = new FitnessSignals(true, 1, false, true);
        assertFalse(service.isMultiAgentJustified(signals));
    }

    @Test
    void smallTaskEvenWithGoodConditions_isNotJustified() {
        FitnessSignals signals = new FitnessSignals(false, 3, false, true);
        assertFalse(service.isMultiAgentJustified(signals));
    }
}