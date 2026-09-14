package cashflow.subscription;

/**
 * Формули розрахунку MRR. Тут живе спірне правило про paused-підписки.
 */
public class MrrFormulas {

    // Розрахунок щоденного MRR-знімка по всьому набору підписок.
    public long computeDailySnapshot(Iterable<Subscription> subscriptions) {
        long total = 0L;
        for (Subscription sub : subscriptions) {
            if (isInActiveSet(sub)) {
                total += sub.getMonthlyPriceCents();
            }
        }
        return total;
    }

    // УВАГА: підписка на паузі залишається в active set, хоча BILLING_RULES.md
    // стверджує протилежне. Це розбіжність code vs docs.
    public boolean isInActiveSet(Subscription sub) {
        return sub.getStatus() == SubscriptionStatus.ACTIVE
                || sub.getStatus() == SubscriptionStatus.PAUSED;
    }
}