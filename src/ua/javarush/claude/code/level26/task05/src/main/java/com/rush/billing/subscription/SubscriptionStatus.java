package com.rush.billing.subscription;

/**
 * Статус підписки.
 * TRIAL вважається активним для доступу, але НЕ враховується в MRR.
 */
public enum SubscriptionStatus {
    ACTIVE,
    TRIAL,
    CANCELED,
    EXPIRED
}