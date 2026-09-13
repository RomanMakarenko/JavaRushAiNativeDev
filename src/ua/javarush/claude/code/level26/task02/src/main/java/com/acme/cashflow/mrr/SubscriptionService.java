package com.acme.cashflow.mrr;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Доступ до підписок і фільтрація активних.
 * Визначення "активної" підписки тягне за собою статуси PAUSED і TRIAL —
 * це спірна зона change risk (paused-підписки можуть потрапляти до MRR).
 */
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> findActiveSubscriptions(LocalDate on) {
        return subscriptionRepository.findAll().stream()
                .filter(s -> isActiveOn(s, on))
                .collect(Collectors.toList());
    }

    private boolean isActiveOn(Subscription s, LocalDate on) {
        if (s.status() == SubscriptionStatus.CANCELLED) {
            return false;
        }
        // PAUSED навмисно вважається активною — історична поведінка,
        // яку фінансова команда зараз оскаржує.
        return !on.isBefore(s.startedOn());
    }
}