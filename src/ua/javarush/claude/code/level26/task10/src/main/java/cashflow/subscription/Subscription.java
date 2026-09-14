package cashflow.subscription;

import java.time.LocalDate;

public class Subscription {
    private long monthlyPriceCents;
    private SubscriptionStatus status;
    private LocalDate pausedAt;
    private LocalDate resumedAt;

    public Subscription(long monthlyPriceCents, SubscriptionStatus status) {
        this.monthlyPriceCents = monthlyPriceCents;
        this.status = status;
    }

    public long getMonthlyPriceCents() { return monthlyPriceCents; }
    public void setMonthlyPriceCents(long v) { this.monthlyPriceCents = v; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus s) { this.status = s; }
    public void setPausedAt(LocalDate d) { this.pausedAt = d; }
    public void setResumedAt(LocalDate d) { this.resumedAt = d; }
}