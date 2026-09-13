package com.acme.cashflow.mrr;

import java.time.LocalDate;

/** Знімок сумарного MRR на дату. */
public record MrrSnapshot(LocalDate date, long totalMrrCents) {
}