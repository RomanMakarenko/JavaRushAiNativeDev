package com.example.store.refunds;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тести сортування повернень для support-toolbar.
 */
class RefundServiceTest {

    private final RefundService service = new RefundService();

    @Test
    void newerRefundsComeFirstWithinSameGroup() {
        RefundView older = new RefundView("r-old", false, Instant.parse("2026-01-01T10:00:00Z"));
        RefundView newer = new RefundView("r-new", false, Instant.parse("2026-01-02T10:00:00Z"));

        List<RefundView> sorted = service.sortForSupport(List.of(older, newer));

        assertThat(sorted).extracting(RefundView::getId)
                .containsExactly("r-new", "r-old");
    }

    @Test
    void manualReviewComesFirstEvenWhenRegularRefundIsNewer() {
        // регресія для REF-482: звичайна заявка новіша за ручну,
        // але ручна все одно має йти вище
        RefundView manualOld = new RefundView("r-manual", true, Instant.parse("2026-01-01T10:00:00Z"));
        RefundView regularNew = new RefundView("r-regular", false, Instant.parse("2026-01-03T10:00:00Z"));

        List<RefundView> sorted = service.sortForSupport(List.of(regularNew, manualOld));

        assertThat(sorted).extracting(RefundView::getId)
                .containsExactly("r-manual", "r-regular");
    }
}