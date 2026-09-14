package com.rush.billing.mrr;

import com.rush.billing.subscription.Subscription;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Обчислює MRR (Monthly Recurring Revenue) за активними підписками.
 *
 * Увага: формули тут — це фактична поведінка legacy-коду,
 * вона може відрізнятися від того, що написано в docs/ARCHITECTURE.md.
 */
@Service
public class MrrCalculationService {

    private final MrrFormulas formulas;

    public MrrCalculationService(MrrFormulas formulas) {
        this.formulas = formulas;
    }

    /**
     * Сумує нормалізований місячний дохід за всіма активними підписками.
     * Підписки у статусі TRIAL у MRR НЕ включаються (важлива деталь legacy-логіки).
     */
    public BigDecimal totalMrr(List<Subscription> subscriptions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Subscription s : subscriptions) {
            if (!s.isActive()) {
                continue;
            }
            if (s.isTrial()) {
                // TRIAL не приносить доходу — пропускаємо
                continue;
            }
            total = total.add(formulas.monthlyAmount(s));
        }
        return total;
    }
}