package com.cashflow.mrr;

/**
 * Збірка допоміжних функцій, накопичених за роки. Кандидат на очищення.
 */
public class LegacyUtils {

    // TODO: дублює логіку округлення з InvoiceService
    public static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // FIXME: використовується лише в одному місці, але експортовано як public
    public static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}