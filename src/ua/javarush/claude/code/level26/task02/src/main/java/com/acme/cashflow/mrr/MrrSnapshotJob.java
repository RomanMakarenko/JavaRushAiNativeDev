package com.acme.cashflow.mrr;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Запланована задача: раз на добу знімає MRR-снапшот за всіма активними підписками.
 * Це основна точка входу для розрахунку MRR у сервісі CashFlow.
 */
@Component
public class MrrSnapshotJob {

    private final SubscriptionService subscriptionService;
    private final MrrCalculator mrrCalculator;
    private final MrrSnapshotRepository snapshotRepository;

    public MrrSnapshotJob(SubscriptionService subscriptionService,
                          MrrCalculator mrrCalculator,
                          MrrSnapshotRepository snapshotRepository) {
        this.subscriptionService = subscriptionService;
        this.mrrCalculator = mrrCalculator;
        this.snapshotRepository = snapshotRepository;
    }

    // Запуск щодня о 02:00 за серверним часом
    @Scheduled(cron = "0 0 2 * * *")
    public void runDailySnapshot() {
        LocalDate today = LocalDate.now();
        List<Subscription> active = subscriptionService.findActiveSubscriptions(today);
        long totalMrrCents = mrrCalculator.totalMrrCents(active, today);
        snapshotRepository.save(new MrrSnapshot(today, totalMrrCents));
    }
}