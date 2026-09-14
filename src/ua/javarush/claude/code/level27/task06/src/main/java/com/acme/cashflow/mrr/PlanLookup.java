package com.acme.cashflow.mrr;

import java.math.BigDecimal;

public interface PlanLookup {

    BigDecimal monthlyPrice(String planId);
}