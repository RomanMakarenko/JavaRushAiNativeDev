package com.example.commerce.refund;

import org.springframework.stereotype.Service;

/**
 * Бізнес-правила refund-flow.
 * Значення manual approval threshold поки що захардкожене як 100 — це кандидат
 * на винесення в конфігурацію в одному з milestone довгого завдання.
 */
@Service
public class RefundPolicyService {

    public boolean requiresManualApproval(int amount) {
        // manual approval threshold захардкожений; у довгому завданні виносимо в конфіг
        return amount > 100;
    }
}