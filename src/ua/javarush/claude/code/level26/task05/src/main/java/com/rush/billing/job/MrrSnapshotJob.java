package com.rush.billing.job;

import com.rush.billing.mrr.MrrCalculationService;
import com.rush.billing.subscription.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Періодично знімає snapshot MRR.
 * Запускається за розкладом (cron), результат поки що лише записується в лог.
 */
@Component
public class MrrSnapshotJob {

    private final MrrCalculationService mrrCalculationService;
    private final SubscriptionService subscriptionService;

    public MrrSnapshotJob(MrrCalculationService mrrCalculationService,
                          SubscriptionService subscriptionService) {
        this.mrrCalculationService = mrrCalculationService;
        this.subscriptionService = subscriptionService;
    }

    /**
     * Обчислює MRR щодня о 02:00.
     * Бере лише активні підписки.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void captureDailySnapshot() {
        BigDecimal mrr = mrrCalculationService.totalMrr(subscriptionService.findActive());
        System.out.println("[MrrSnapshotJob] daily MRR snapshot = " + mrr);
    }
}