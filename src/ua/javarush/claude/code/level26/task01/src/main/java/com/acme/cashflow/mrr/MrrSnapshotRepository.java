package com.acme.cashflow.mrr;

import java.time.LocalDate;
import java.util.Optional;

/** Сховище MRR-снапшотів. */
public interface MrrSnapshotRepository {
    void save(MrrSnapshot snapshot);
    Optional<MrrSnapshot> findByDate(LocalDate date);
}