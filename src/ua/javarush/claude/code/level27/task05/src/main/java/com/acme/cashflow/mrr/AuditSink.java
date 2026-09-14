package com.acme.cashflow.mrr;

/**
 * Побічний ефект запису audit-подій.
 * Наразі викликається напряму з розрахункового коду — це side-effect seam.
 */
public class AuditSink {

    public void record(String event, String details) {
        // в legacy-реалізації просто пишемо в stdout;
        // важливо, що це саме side effect всередині розрахунку
        System.out.println("[AUDIT] " + event + " :: " + details);
    }
}