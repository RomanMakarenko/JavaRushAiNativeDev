package com.acme.workflow;

// Вхідні сигнали для оцінювання придатності multi-agent.
// Public API: набір полів змінювати не можна.
public class FitnessSignals {

    private final boolean large;
    private final int independentWorkstreams;
    private final boolean sharedStateHigh;
    private final boolean testsAvailable;

    public FitnessSignals(boolean large,
                          int independentWorkstreams,
                          boolean sharedStateHigh,
                          boolean testsAvailable) {
        this.large = large;
        this.independentWorkstreams = independentWorkstreams;
        this.sharedStateHigh = sharedStateHigh;
        this.testsAvailable = testsAvailable;
    }

    public boolean isLarge() {
        return large;
    }

    public int getIndependentWorkstreams() {
        return independentWorkstreams;
    }

    public boolean isSharedStateHigh() {
        return sharedStateHigh;
    }

    public boolean isTestsAvailable() {
        return testsAvailable;
    }
}