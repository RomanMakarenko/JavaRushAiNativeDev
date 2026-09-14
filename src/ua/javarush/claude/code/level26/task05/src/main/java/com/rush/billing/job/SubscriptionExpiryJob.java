package com.rush.billing.job;

import com.rush.billing.subscription.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Позначає прострочені підписки.
 * Запускається частіше, ніж snapshot-job.
 */
@Component
public class SubscriptionExpiryJob {

    private final SubscriptionService subscriptionService;

    public SubscriptionExpiryJob(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Перевіряє прострочені підписки щогодини.
     * Наразі фактично лише перебирає підписки без запису статусу.
     */
    @Scheduled(fixedRate = 3600000)
    public void markExpired() {
        int active = subscriptionService.findActive().size();
        System.out.println("[SubscriptionExpiryJob] checked active subscriptions = " + active);
    }
}