package com.acme.cashflow.mrr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Точка зберігання прапорця увімкнення нового шляху для single-plan slice.
 * false за замовчуванням = безпечний стан = працюємо legacy-шляхом.
 */
@Component
public class MrrFeatureFlags {

    @Value("${cashflow.mrr.v2-single-plan:false}")
    private boolean v2SinglePlan;

    public boolean useV2ForSinglePlan() {
        return v2SinglePlan;
    }
}