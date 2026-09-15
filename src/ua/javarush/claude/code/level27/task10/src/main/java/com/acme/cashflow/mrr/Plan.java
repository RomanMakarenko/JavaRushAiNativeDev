package com.acme.cashflow.mrr;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Підписковий план: тип, ціна та період дії. */
public record Plan(String id, String type, BigDecimal monthlyPrice, LocalDate startDate, LocalDate endDate) {
}