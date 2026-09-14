package com.acme.cashflow.mrr;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Старий шлях пошуку тарифного плану.
 * Це еталон поведінки: його спостережуваний результат вважається «правдою» на час slice.
 * Відому дивину (yearly зберігається як 12 * місячна ціна без знижки)
 * зберігаємо як частину сумісності — на неї зав'язані зовнішні звіти.
 */
@Component
public class LegacyPlanLookup {

    private final Map<String, PlanInfo> table = new HashMap<>();

    public LegacyPlanLookup() {
        table.put("basic-month", new PlanInfo("basic-month", 49900L, "monthly"));
        table.put("pro-month", new PlanInfo("pro-month", 99900L, "monthly"));
        table.put("pro-year", new PlanInfo("pro-year", 1198800L, "yearly"));
    }

    public PlanInfo find(String code) {
        PlanInfo info = table.get(code);
        if (info == null) {
            // Legacy-поведінка: невідомий код повертає нульовий план, а не кидає виняток.
            return new PlanInfo(code, 0L, "unknown");
        }
        return info;
    }
}