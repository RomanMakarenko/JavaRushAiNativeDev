package com.acme.cashflow.mrr;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Новий шлях пошуку тарифного плану для single-plan slice.
 * Мета Strangler Fig — видавати той самий спостережуваний результат, що і legacy,
 * включно зі старою дивиною з yearly. Бізнес-правила тут не змінюємо.
 */
@Component
public class PlanLookupV2 {

    private final Map<String, PlanInfo> table = new HashMap<>();

    public PlanLookupV2() {
        table.put("basic-month", new PlanInfo("basic-month", 49900L, "monthly"));
        table.put("pro-month", new PlanInfo("pro-month", 99900L, "monthly"));
        table.put("pro-year", new PlanInfo("pro-year", 1198800L, "yearly"));
    }

    public PlanInfo find(String code) {
        PlanInfo info = table.get(code);
        if (info == null) {
            // Повторюємо legacy-контракт: невідомий код = нульовий план.
            return new PlanInfo(code, 0L, "unknown");
        }
        return info;
    }
}