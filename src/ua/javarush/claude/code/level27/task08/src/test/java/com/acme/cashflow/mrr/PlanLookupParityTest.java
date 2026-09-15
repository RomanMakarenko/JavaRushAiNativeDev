package com.acme.cashflow.mrr;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Порівнює спостережуваний результат legacy та v2 шляхів пошуку тарифного плану.
 */
class PlanLookupParityTest {

    @Test
    void legacyAndV2HaveTheSameObservableOutput() {
        LegacyPlanLookup legacy = new LegacyPlanLookup();
        PlanLookupV2 v2 = new PlanLookupV2();

        for (String code : fixtureCodes()) {
            PlanInfo legacyResult = legacy.find(code);
            PlanInfo v2Result = v2.find(code);

            assertAll(code,
                    () -> assertEquals(legacyResult.getCode(), v2Result.getCode()),
                    () -> assertEquals(legacyResult.getMrr(), v2Result.getMrr()),
                    () -> assertEquals(legacyResult.getBillingPeriod(), v2Result.getBillingPeriod()));
        }
    }

    private List<String> fixtureCodes() {
        InputStream input = getClass().getResourceAsStream("/fixtures/lookup/plan-codes.csv");
        assertNotNull(input, "Parity fixture must be available on the test classpath");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read parity fixture", exception);
        }
    }
}