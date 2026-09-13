package com.acme.cashflow.mrr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Інтеграційний тест щоденного MRR-снапшота.
 * Покриває лише happy-path з MONTHLY-підписками. YEARLY-нормалізація
 * і поведінка PAUSED-підписок тестами НЕ покриті.
 */
@SpringBootTest
class MrrSnapshotIT {

    @Autowired
    private MrrSnapshotJob mrrSnapshotJob;

    @Autowired
    private MrrSnapshotRepository snapshotRepository;

    @Test
    void runDailySnapshot_savesSnapshotForToday() {
        mrrSnapshotJob.runDailySnapshot();
        assertTrue(snapshotRepository.findByDate(LocalDate.now()).isPresent());
    }
}