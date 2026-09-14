package com.acme.cashflow.mrr;

import java.util.Objects;

/**
 * Спостережуваний результат пошуку тарифного плану в mrr-engine.
 * Саме ці поля порівнює parity-перевірка між legacy і v2 шляхами.
 */
public class PlanInfo {

    private final String code;
    private final long mrr;            // monthly recurring revenue у копійках
    private final String billingPeriod; // наприклад "monthly" / "yearly"

    public PlanInfo(String code, long mrr, String billingPeriod) {
        this.code = code;
        this.mrr = mrr;
        this.billingPeriod = billingPeriod;
    }

    public String getCode() {
        return code;
    }

    public long getMrr() {
        return mrr;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanInfo)) {
            return false;
        }
        PlanInfo planInfo = (PlanInfo) o;
        return mrr == planInfo.mrr
                && Objects.equals(code, planInfo.code)
                && Objects.equals(billingPeriod, planInfo.billingPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, mrr, billingPeriod);
    }

    @Override
    public String toString() {
        return "PlanInfo{code='" + code + "', mrr=" + mrr + ", billingPeriod='" + billingPeriod + "'}";
    }
}