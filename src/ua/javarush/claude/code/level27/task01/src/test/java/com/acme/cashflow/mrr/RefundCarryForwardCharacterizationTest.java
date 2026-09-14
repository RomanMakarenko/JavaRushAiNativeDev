package com.acme.cashflow.mrr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

// Characterization test для legacy-quirk: refund поточного місяця переходить у MRR наступного місяця.
// Тест має ФІКСУВАТИ поточну поведінку на fixture refund-same-period.json,
// не виправляючи її. Production code (MrrCalculator) змінювати не можна.
class RefundCarryForwardCharacterizationTest {

    private static final String FIXTURE = "/fixtures/refund-same-period.json";
    private static final Pattern EVENT = Pattern.compile(
            "\\\"type\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*"
                    + "\\\"date\\\"\\s*:\\s*\\\"[^\\\"]+\\\"\\s*,\\s*"
                    + "\\\"amount\\\"\\s*:\\s*(-?\\d+)");
    private static final Pattern MRR_ENTRY = Pattern.compile(
            "\\\"(\\d{4}-\\d{2})\\\"\\s*:\\s*(-?\\d+)");

    @Test
    void refundInCurrentPeriodIsCarriedForwardToNextMonth() throws IOException {
        String fixture = readFixture();
        String period = stringValue(fixture, "period");
        int monthlyAmount = intValue(fixture, "monthlyAmount");

        List<MrrCalculator.Event> events = new ArrayList<>();
        Matcher eventMatcher = EVENT.matcher(fixture);
        while (eventMatcher.find()) {
            events.add(new MrrCalculator.Event(eventMatcher.group(1), Integer.parseInt(eventMatcher.group(2))));
        }

        Map<String, Integer> expected = expectedMrr(fixture);
        Map<String, Integer> actual = new MrrCalculator().calculate(period, monthlyAmount, events);
        String nextPeriod = MrrCalculator.nextPeriod(period);

        assertEquals(expected.get(period), actual.get(period), "поточний місяць має відповідати fixture");
        assertEquals(expected.get(nextPeriod), actual.get(nextPeriod),
                "наступний місяць має зафіксувати carry-forward refund");
        assertEquals(expected, actual, "результат має повністю відповідати characterization fixture");
    }

    private static String readFixture() throws IOException {
        try (InputStream input = RefundCarryForwardCharacterizationTest.class.getResourceAsStream(FIXTURE)) {
            assertNotNull(input, "fixture не знайдено: " + FIXTURE);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String stringValue(String fixture, String field) {
        Matcher matcher = Pattern.compile("\\\"" + field + "\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").matcher(fixture);
        assertNotNull(matcher.find() ? matcher : null, "поле fixture не знайдено: " + field);
        return matcher.group(1);
    }

    private static int intValue(String fixture, String field) {
        Matcher matcher = Pattern.compile("\\\"" + field + "\\\"\\s*:\\s*(-?\\d+)").matcher(fixture);
        assertNotNull(matcher.find() ? matcher : null, "поле fixture не знайдено: " + field);
        return Integer.parseInt(matcher.group(1));
    }

    private static Map<String, Integer> expectedMrr(String fixture) {
        Map<String, Integer> expected = new LinkedHashMap<>();
        int mrrStart = fixture.indexOf("\"mrr\"");
        Matcher matcher = MRR_ENTRY.matcher(fixture.substring(mrrStart));
        while (matcher.find()) {
            expected.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }
        return expected;
    }
}