package com.example.store.refunds;

import java.time.Instant;

/**
 * DTO для відображення повернення в support-toolbar.
 * Публічний контракт відповіді ендпойнта списку повернень.
 */
public final class RefundView {

    private final String id;
    private final boolean manualReview;
    private final Instant createdAt;

    public RefundView(String id, boolean manualReview, Instant createdAt) {
        this.id = id;
        this.manualReview = manualReview;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public boolean isManualReview() {
        return manualReview;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}