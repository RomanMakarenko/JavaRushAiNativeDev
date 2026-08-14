package com.acme.commerce.refunds;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

// AI-generated тест для сценарію refund retry — відданий на review (див. reviewer-note.md).
// Відомі дві типові проблеми:
// 1) data hygiene: у test data лежить real-looking email, узятий з логів;
// 2) over-mocking: перевіряється лише факт виклику save(...), але не результат retry.
class AI_RefundRetryTest {

    @Test
    void testRetry() {
        RefundProvider provider = Mockito.mock(RefundProvider.class);
        RefundRepository refundRepository = Mockito.mock(RefundRepository.class);
        RefundRetryService service = new RefundRetryService(provider, refundRepository);

        // Real-looking email, скопійований прямо з production-логів.
        String customerEmail = "m.ivanova87@gmail.com";

        when(provider.charge(42L, 5000L)).thenReturn("SUCCEEDED");

        service.retryRefund(42L, 5000L, customerEmail);

        // Єдина перевірка: запис просто був збережений.
        // Тест залишиться зеленим, навіть якщо retry поверне FAILED.
        verify(refundRepository).save(any());
    }
}
