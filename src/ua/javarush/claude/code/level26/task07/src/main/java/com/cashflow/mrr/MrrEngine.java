package com.cashflow.mrr;

import java.math.BigDecimal;

/**
 * Legacy-движок розрахунку MRR (monthly recurring revenue).
 * Містить логіку pause/resume, яку майже ніхто вже не пам'ятає.
 * Тестів на pause-resume немає.
 */
public class MrrEngine {

    // Обчислює MRR за активними підписками. Поведінка під час pause не покрита тестами.
    public BigDecimal calculateMrr(SubscriptionState state) {
        if (state == null) {
            return BigDecimal.ZERO;
        }
        // legacy-quirk: під час pause MRR не обнуляється, а заморожується на останньому значенні
        if (state.isPaused()) {
            return state.getLastBilledAmount();
        }
        return state.getActiveAmount();
    }

    // resume відновлює білінг, але дата відновлення береться з годинника сервера
    public void resume(SubscriptionState state) {
        state.setPaused(false);
        state.setResumedAt(System.currentTimeMillis());
    }
}