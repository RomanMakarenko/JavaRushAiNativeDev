package cashflow.subscription;

import java.time.LocalDate;

/**
 * Legacy-сервіс керування підписками CashFlow Dashboard.
 * Java 8, Spring Boot 2.7. Частина бізнес-правил живе тут,
 * частина розмазана по MrrFormulas і RefundService.
 */
public class SubscriptionService {

    private final MrrFormulas mrrFormulas;
    private final RefundService refundService;

    public SubscriptionService(MrrFormulas mrrFormulas, RefundService refundService) {
        this.mrrFormulas = mrrFormulas;
        this.refundService = refundService;
    }

    // Поставлення підписки на паузу. Статус змінюється, але з active MRR
    // підписка НЕ видаляється (див. MrrFormulas.isInActiveSet).
    public void pause(Subscription sub, LocalDate day) {
        sub.setStatus(SubscriptionStatus.PAUSED);
        sub.setPausedAt(day);
    }

    // Відновлення підписки після паузи. Сума MRR не перераховується.
    public void resume(Subscription sub, LocalDate day) {
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setResumedAt(day);
    }

    // Часткове повернення в середині періоду делегується в RefundService.
    public long refund(Subscription sub, long amountCents, LocalDate day) {
        long applied = refundService.applyPartialRefund(sub, amountCents, day);
        return applied;
    }
}