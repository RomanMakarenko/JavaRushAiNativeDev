package com.rush.commerce.refund;

// Сервіс оброблення черги повернень.
// У межах поточного завдання product code НЕ змінюється — робота ведеться лише в sandbox-worktree.
public class RefundQueueService {

    private final RefundRepository repository;

    public RefundQueueService(RefundRepository repository) {
        this.repository = repository;
    }

    // Позначає завислі повернення як такі, що потребують ручної перевірки.
    public int flagStuckRefunds() {
        return repository.markStuckForReview();
    }
}