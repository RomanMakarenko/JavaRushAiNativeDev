package com.acme.workflow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Поточні тести перевіряють лише великий happy-path і не ловлять помилку
// з high shared state та відсутністю tests.
class MultiAgentFitnessServiceTest {

    private final MultiAgentFitnessService service = new MultiAgentFitnessService();

    @Test
    void largeTaskWithSeparateStreamsAndTests_isJustified() {
        FitnessSignals signals = new FitnessSignals(true, 3, false, true);
        assertTrue(service.isMultiAgentJustified(signals));
    }
}