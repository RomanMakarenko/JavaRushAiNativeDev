package com.acme.commerce.refunds;

// Бізнес-логіка повернення. Тут має перевірятися ідемпотентність за ключем запиту.
public class RefundService {

    private final RefundRepository repository;
    private final RefundPolicy policy;

    public RefundService(RefundRepository repository, RefundPolicy policy) {
        this.repository = repository;
        this.policy = policy;
    }

    public RefundResult processRefund(RefundRequest request) {
        // Увага: повторне надсилання може призводити до дублюючого запису в ledger.
        policy.validate(request);
        return repository.save(request);
    }
}
