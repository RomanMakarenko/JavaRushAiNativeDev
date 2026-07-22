package com.example.store.refunds;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Сервіс повернень: готує список заявок для support-toolbar.
 * Тут же живе логіка сортування, на яку скаржиться issue REF-482.
 */
@Service
public class RefundService {

    /**
     * Сортує повернення для відображення в support-toolbar.
     * Спочатку заявки з manualReview=true, потім звичайні;
     * усередині кожної групи новіші йдуть раніше за старі.
     */
    public List<RefundView> sortForSupport(List<RefundView> refunds) {
        Comparator<RefundView> byManualReviewFirst =
                Comparator.comparing(RefundView::isManualReview).reversed();
        Comparator<RefundView> byNewestFirst =
                Comparator.comparing(RefundView::getCreatedAt).reversed();

        return refunds.stream()
                .sorted(byManualReviewFirst.thenComparing(byNewestFirst))
                .collect(Collectors.toList());
    }

    static final class Refund {
        private final String id;
        private final boolean manualReview;
        private final Instant createdAt;

        Refund(String id, boolean manualReview, Instant createdAt) {
            this.id = id;
            this.manualReview = manualReview;
            this.createdAt = createdAt;
        }

        String getId() {
            return id;
        }

        boolean isManualReview() {
            return manualReview;
        }

        Instant getCreatedAt() {
            return createdAt;
        }
    }
}