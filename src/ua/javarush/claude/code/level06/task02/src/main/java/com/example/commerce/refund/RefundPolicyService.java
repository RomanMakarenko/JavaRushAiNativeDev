package com.example.commerce.refund;

import org.springframework.stereotype.Service;

/**
 * Бізнес-правила refund-flow.
 * Значення порогу ручного схвалення поки захардкожене як 100 — це кандидат
 * на винесення в конфігурацію в одному з milestone довгої задачі.
 */
@Service
public class RefundPolicyService {

    public boolean requiresManualApproval(int amount) {
        // Поріг ручного схвалення захардкожено; у довгій задачі виносимо в конфіг
        return amount > 100;
    }
}