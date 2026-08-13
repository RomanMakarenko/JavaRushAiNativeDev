package com.acme.commerce.refunds;

// Бізнес-логіка повернення. Тут має перевірятися ідемпотентність за ключем запиту.
public class RefundService {

    private final RefundRepository repository;

    public RefundService(RefundRepository repository) {
        this.repository = repository;
    }

    public RefundResult processRefund(RefundRequest request) {
        // Увага: повторне надсилання може призводити до дубльованого запису в ledger.
        return repository.save(request);
    }
}