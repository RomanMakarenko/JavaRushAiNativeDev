package com.acme.commerce.refunds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

// Виправлена версія AI-згенерованого тесту для сценарію refund retry (див. reviewer-note.md).
// Виправлення:
// 1) data hygiene: real-looking email з production-логів замінено на нейтральний плейсхолдер test@example.com;
// 2) over-mocking: перевіряється підсумковий business результат — retry доводить статус до SUCCEEDED,
//    а не лише факт виклику save(...).
class CORRECTED_RefundRetryTest {

    @Test
    void retryRefundRecoversWhenFirstAttemptFails() {
        RefundProvider provider = Mockito.mock(RefundProvider.class);
        RefundRepository refundRepository = Mockito.mock(RefundRepository.class);
        RefundRetryService service = new RefundRetryService(provider, refundRepository);

        String customerEmail = "test@example.com";

        // Сценарій: перша спроба у провайдера падає (тимчасова помилка), retry доводить до успіху.
        when(provider.charge(42L, 5000L)).thenReturn("FAILED", "SUCCEEDED");

        RefundResult result = service.retryRefund(42L, 5000L, customerEmail);

        // Assertion на business behavior: підсумковий статус повернення після retry — SUCCEEDED.
        assertEquals("SUCCEEDED", result.status());
    }
}