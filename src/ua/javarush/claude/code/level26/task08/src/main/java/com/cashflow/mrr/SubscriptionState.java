package com.cashflow.mrr;

import java.math.BigDecimal;

/** Стан підписки для обчислення MRR. */
public class SubscriptionState {

    private boolean paused;
    private BigDecimal activeAmount = BigDecimal.ZERO;
    private BigDecimal lastBilledAmount = BigDecimal.ZERO;
    private long resumedAt;

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public BigDecimal getActiveAmount() {
        return activeAmount;
    }

    public void setActiveAmount(BigDecimal activeAmount) {
        this.activeAmount = activeAmount;
    }

    public BigDecimal getLastBilledAmount() {
        return lastBilledAmount;
    }

    public void setLastBilledAmount(BigDecimal lastBilledAmount) {
        this.lastBilledAmount = lastBilledAmount;
    }

    public long getResumedAt() {
        return resumedAt;
    }

    public void setResumedAt(long resumedAt) {
        this.resumedAt = resumedAt;
    }
}