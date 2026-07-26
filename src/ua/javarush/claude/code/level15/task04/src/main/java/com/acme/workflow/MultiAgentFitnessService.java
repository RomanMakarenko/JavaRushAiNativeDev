package com.acme.workflow;

// Сервіс оцінювання придатності multi-agent.
// ПОМИЛКА: зараз сервіс повертає true для будь-якого large-завдання і ігнорує
// shared state, відсутність tests та кількість незалежних workstreams.
public class MultiAgentFitnessService {

    public boolean isMultiAgentJustified(FitnessSignals signals) {
        // Неправильна логіка: розмір завдання — єдиний критерій.
        return signals.isLarge();
    }
}