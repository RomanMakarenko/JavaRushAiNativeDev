package com.acme.workflow;

// Сервіс оцінювання придатності multi-agent.
// ПОМИЛКА: зараз сервіс повертає true для будь-якого large-завдання і ігнорує
// shared state, відсутність tests та кількість незалежних workstreams.
public class MultiAgentFitnessService {

    public boolean isMultiAgentJustified(FitnessSignals signals) {
        if (signals.isSharedStateHigh()) {
            return false;
        }
        if (!signals.isTestsAvailable()) {
            return false;
        }
        return signals.isLarge() && signals.getIndependentWorkstreams() >= 2;
    }
}