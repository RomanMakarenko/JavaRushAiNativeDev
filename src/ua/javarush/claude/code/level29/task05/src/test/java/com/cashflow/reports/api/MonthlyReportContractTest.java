package com.cashflow.reports.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonthlyReportContractTest {

    private static final String BASELINE_RESOURCE = "/baseline/monthly-report.json";
    private static final String MONTHLY_REPORT_ENDPOINT = "/api/reports/monthly?period=2026-04";

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void monthlyReportMatchesBaselineSnapshot() throws IOException {
        ResponseEntity<String> actualResponse = rest.getForEntity(
                "http://localhost:" + port + MONTHLY_REPORT_ENDPOINT,
                String.class);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertNotNull(actualResponse.getBody());

        JsonNode baseline = readBaseline();
        JsonNode actual = objectMapper.readTree(actualResponse.getBody());

        assertEquals(baseline, actual, "Monthly report response differs from baseline snapshot");
    }

    private JsonNode readBaseline() throws IOException {
        try (InputStream baselineStream = getClass().getResourceAsStream(BASELINE_RESOURCE)) {
            assertNotNull(baselineStream, "Missing baseline resource: " + BASELINE_RESOURCE);
            return objectMapper.readTree(baselineStream);
        }
    }
}