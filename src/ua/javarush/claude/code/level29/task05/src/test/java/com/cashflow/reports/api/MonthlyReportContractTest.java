package com.cashflow.reports.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: додати contract test, що порівнює actual response /api/reports/monthly
// з baseline snapshot src/test/resources/baseline/monthly-report.json.
// Перевіряти потрібно вміст response (функціональний паритет), а не лише status code.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonthlyReportContractTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;
}